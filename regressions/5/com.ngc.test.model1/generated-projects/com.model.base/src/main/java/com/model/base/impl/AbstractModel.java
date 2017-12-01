package com.model.base.impl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.model.api.IModel;
import com.model.event.Data1;
import com.model.event.Data2;
import com.model.event.Data4;
import com.model.event.Data5;
import com.ngc.blocs.api.IContext;
import com.ngc.blocs.api.IStatus;
import com.ngc.blocs.requestmodel.api.IRequest;
import com.ngc.blocs.requestmodel.api.Requests;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.api.ServiceStatus;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.Subscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.thread.api.ISubmittedLongLivingTask;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.seaside.request.api.ServiceRequest;
import com.ngc.seaside.service.correlation.api.ICorrelationService;
import com.ngc.seaside.service.correlation.api.ICorrelationStatus;
import com.ngc.seaside.service.correlation.api.ICorrelationTrigger;
import com.ngc.seaside.service.correlation.api.ILocalCorrelationEvent;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public abstract class AbstractModel
   implements IServiceModule, IModel {

   public final static String NAME = "service:com.Model";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   protected ICorrelationService correlationService;

   protected Map<ICorrelationTrigger<?>, Collection<Consumer<ICorrelationStatus<?>>>> triggers = new ConcurrentHashMap<>();

   private Map<Class<?>, Collection<Queue<?>>> queues = new ConcurrentHashMap<>();

   private Map<String, ISubmittedLongLivingTask> threads = new ConcurrentHashMap<>();

   @Subscriber(Data1.TOPIC_NAME)
   public void receiveData1(IEvent<Data1> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      Data1 source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doBasicPubSub(source);
      doBasicSink(source);
      correlationService.correlate(source)
         .stream()
         .filter(ICorrelationStatus::isCorrelationComplete)
         .forEach(status -> {
            triggers.get(status.getTrigger()).forEach(consumer -> consumer.accept(status));
         });
      queues.getOrDefault(Data1.class, Collections.emptyList()).forEach(queue -> {
         @SuppressWarnings("unchecked")
         Collection<Data1> q = (Collection<Data1>) queue;
         q.add(source);
      });
   }

   @Subscriber(Data2.TOPIC_NAME)
   public void receiveData2(IEvent<Data2> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      Data2 source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      correlationService.correlate(source)
         .stream()
         .filter(ICorrelationStatus::isCorrelationComplete)
         .forEach(status -> {
            triggers.get(status.getTrigger()).forEach(consumer -> consumer.accept(status));
         });
      queues.getOrDefault(Data2.class, Collections.emptyList()).forEach(queue -> {
         @SuppressWarnings("unchecked")
         Collection<Data2> q = (Collection<Data2>) queue;
         q.add(source);
      });
   }

   private void publishData4(Data4 value) {
      Preconditions.checkNotNull(value, "Data4 value may not be null!");
      eventService.publish(value, Data4.TOPIC);
   }

   private void publishData5(Data5 value) {
      Preconditions.checkNotNull(value, "Data5 value may not be null!");
      eventService.publish(value, Data5.TOPIC);
   }

   private void doBasicPubSub(Data1 input) {
      Data4 output;
      try {
         output = basicPubSub(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractModel.basicPubSub' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: basicPubSub; Input: %s; Output: %s;", input, output);
      publishData4(output);
   }

   private void doBasicSink(Data1 input) {
      try {
         basicSink(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractModel.basicSink' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: ${method.scenarioName}; Input: %s; Output: ;", input);
   }

   private void doSingleOutputCorrelation(ICorrelationStatus<?> status) {
      updateRequestWithCorrelation(status.getEvent());
      try {
         @SuppressWarnings("unchecked")
         Data4 output = singleOutputCorrelation(
               status.getData(Data1.class),
               status.getData(Data2.class),
               (ILocalCorrelationEvent<Integer>) status.getEvent());
         output.getField5().setField6(status.getData(Data1.class).getField3().getField4());
         output.setField2(status.getData(Data2.class).getField1());
         logService.info(getClass(), "ELK - Scenario: singleOutputCorrelation; Input: %s, %s; Output: %s;",
            status.getData(Data1.class).toString(),
            status.getData(Data2.class).toString(),
            output.toString());
         publishData4(output);
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
                  "Invocation of 'AbstractModel.singleOutputCorrelation' generated a fault, dispatching to fault management service.");
         faultManagementService.handleFault(fault);
      } finally {
         clearCorrelationFromRequest();
      }
   }

   private void registerSingleOutputCorrelationTrigger() {
      ICorrelationTrigger<Integer> trigger = correlationService.newTrigger(Integer.class)
            .addEventIdProducer(Data1.class, a -> a.getField1())
            .addEventIdProducer(Data2.class, a -> a.getField5())
            .addCompletenessCondition(Data1.class, Data2.class, (a, b) ->
               Objects.equal(a.getField1(), b.getField5()))
            .register();
      triggers.computeIfAbsent(trigger, __ -> new ArrayList<>()).add(this::doSingleOutputCorrelation);
   }

   private void startMultiOutputPubSub() {
      threads.put("multiOutputPubSub", threadService.executeLongLivingTask("multiOutputPubSub", () -> {
         final BlockingQueue<Data1> input1Queue = new LinkedBlockingQueue<>();
         queues.computeIfAbsent(Data1.class, __ -> Collections.newSetFromMap(new IdentityHashMap<>())).add(input1Queue);
         try {
            multiOutputPubSub(input1Queue, AbstractModel.this::publishData4, AbstractModel.this::publishData5);
         } finally {
            threads.remove("multiOutputPubSub");
            queues.getOrDefault(Data1.class, Collections.emptySet()).remove(input1Queue);
         }
      }));
   }
   private void startComplexPubSub() {
      threads.put("complexPubSub", threadService.executeLongLivingTask("complexPubSub", () -> {
         final BlockingQueue<Data1> input1Queue = new LinkedBlockingQueue<>();
         final BlockingQueue<Data2> input2Queue = new LinkedBlockingQueue<>();
         queues.computeIfAbsent(Data1.class, __ -> Collections.newSetFromMap(new IdentityHashMap<>())).add(input1Queue);
         queues.computeIfAbsent(Data2.class, __ -> Collections.newSetFromMap(new IdentityHashMap<>())).add(input2Queue);
         try {
            complexPubSub(input1Queue, input2Queue, AbstractModel.this::publishData4, AbstractModel.this::publishData5);
         } finally {
            threads.remove("complexPubSub");
            queues.getOrDefault(Data1.class, Collections.emptySet()).remove(input1Queue);
            queues.getOrDefault(Data2.class, Collections.emptySet()).remove(input2Queue);
         }
      }));
   }
   private void startBasicSource() {
      threads.put("basicSource", threadService.executeLongLivingTask("basicSource", () -> {
         try {
            basicSource(AbstractModel.this::publishData4);
         } finally {
            threads.remove("basicSource");
         }
      }));
   }
   private void startMultiSource() {
      threads.put("multiSource", threadService.executeLongLivingTask("multiSource", () -> {
         try {
            multiSource(AbstractModel.this::publishData4, AbstractModel.this::publishData5);
         } finally {
            threads.remove("multiSource");
         }
      }));
   }
   protected void activate() {
      registerSingleOutputCorrelationTrigger();
      startMultiOutputPubSub();
      startComplexPubSub();
      startBasicSource();
      startMultiSource();
      eventService.addSubscriber(this);
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

   protected void deactivate() {
      eventService.removeSubscriber(this);
      triggers.keySet().forEach(ICorrelationTrigger::unregister);
      triggers.clear();
      queues.clear();
      threads.values().forEach(ISubmittedLongLivingTask::cancel);
      threads.clear();
      setStatus(ServiceStatus.DEACTIVATED);
      logService.info(getClass(), "deactivated");
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IContext<?> getContext() {
      return context;
   }

   @Override
   public void setContext(@SuppressWarnings("rawtypes") IContext context) {
      this.context = context;
   }

   @Override
   public IStatus<ServiceStatus> getStatus() {
      return status;
   }

   @Override
   public boolean setStatus(IStatus<ServiceStatus> status) {
      Preconditions.checkNotNull(status, "status may not be null!");
      this.status = status.getStatus();
      return true;
   }

   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   public void setEventService(IEventService ref) {
      this.eventService = ref;
   }

   public void removeEventService(IEventService ref) {
      setEventService(null);
   }

   public void setCorrelationService(ICorrelationService ref) {
      this.correlationService = ref;
   }

   public void removeCorrelationService(ICorrelationService ref) {
      setCorrelationService(null);
   }

   public void setFaultManagementService(IFaultManagementService ref) {
      this.faultManagementService = ref;
   }

   public void removeFaultManagementService(IFaultManagementService ref) {
      setFaultManagementService(null);
   }

   public void setThreadService(IThreadService ref) {
      this.threadService = ref;
   }

   public void removeThreadService(IThreadService ref) {
      setThreadService(null);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private void updateRequestWithCorrelation(ILocalCorrelationEvent<?> event) {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof ServiceRequest) {
         ((ServiceRequest) request).setLocalCorrelationEvent(event);
      }
   }

   @SuppressWarnings("rawtypes")
   private void clearCorrelationFromRequest() {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof ServiceRequest) {
         ((ServiceRequest) request).clearLocalCorrelationEvent();
      }
   }
}
