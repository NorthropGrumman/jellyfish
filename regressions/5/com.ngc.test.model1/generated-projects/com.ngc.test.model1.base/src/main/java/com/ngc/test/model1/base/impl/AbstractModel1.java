package com.ngc.test.model1.base.impl;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ngc.blocs.api.IContext;
import com.ngc.blocs.api.IStatus;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.api.ServiceStatus;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.Subscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.blocs.service.thread.api.ISubmittedLongLivingTask;

import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.test.model1.api.IModel1;
import com.ngc.test.model1.event.Data1;
import com.ngc.test.model1.event.Data2;
import com.ngc.test.model1.event.Data5;
import com.ngc.test.model1.event.test2.Data3;
import com.ngc.test.model1.event.test2.Data4;

public abstract class AbstractModel1
   implements IServiceModule, IModel1 {

   public final static String NAME = "service:com.ngc.test.Model1";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   protected Map<String, ISubmittedLongLivingTask> threads = new ConcurrentHashMap<>();

   @Subscriber(Data1.TOPIC_NAME)
   public void receiveData1(IEvent<Data1> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         duplicateSink(event.getSource());
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.duplicateSink(Data1)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

      try {
         sink1(event.getSource());
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.sink1(Data1)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

      try {
         publishData1(pubsub1(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.pubsub1(Data1)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

      try {
         publishData1(duplicatePubsub(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.duplicatePubsub(Data1)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

   }

   @Subscriber(Data2.TOPIC_NAME)
   public void receiveData2(IEvent<Data2> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         publishData4(pubsub2(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.pubsub2(Data2)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

   }

   @Subscriber(Data3.TOPIC_NAME)
   public void receiveData3(IEvent<Data3> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         sink2(event.getSource());
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.sink2(Data3)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

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
   public void setContext(@SuppressWarnings("rawtypes") IContext iContext) {
      this.context = iContext;
   }

   @Override
   public IStatus<ServiceStatus> getStatus() {
      return status;
   }

   @Override
   public boolean setStatus(IStatus<ServiceStatus> iStatus) {
      Preconditions.checkNotNull(iStatus, "iStatus may not be null!");
      this.status = iStatus.getStatus();
      return true;
   }

   protected void activate() {
      eventService.addSubscriber(this);

      threads.put("source1::publishData1", threadService.executeLongLivingTask("source1::publishData1", () -> {
         try {
            source1(AbstractModel1.this::publishData1);
         } catch (ServiceFaultException fault) {
            logService.error(getClass(),
               "Invocation of '%s.source1(Consumer<output1>)' generated fault, dispatching to fault management service.",
               getClass().getName());
            faultManagementService.handleFault(fault);
            // Consume exception.
         } finally {
            threads.remove("source1::publishData1");
         }
      }));

      threads.put("duplicateSource::publishData1", threadService.executeLongLivingTask("duplicateSource::publishData1", () -> {
         try {
            duplicateSource(AbstractModel1.this::publishData1);
         } catch (ServiceFaultException fault) {
            logService.error(getClass(),
               "Invocation of '%s.duplicateSource(Consumer<output1>)' generated fault, dispatching to fault management service.",
               getClass().getName());
            faultManagementService.handleFault(fault);
            // Consume exception.
         } finally {
            threads.remove("duplicateSource::publishData1");
         }
      }));

      threads.put("source2::publishData5", threadService.executeLongLivingTask("source2::publishData5", () -> {
         try {
            source2(AbstractModel1.this::publishData5);
         } catch (ServiceFaultException fault) {
            logService.error(getClass(),
               "Invocation of '%s.source2(Consumer<output3>)' generated fault, dispatching to fault management service.",
               getClass().getName());
            faultManagementService.handleFault(fault);
            // Consume exception.
         } finally {
            threads.remove("source2::publishData5");
         }
      }));

      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

   protected void deactivate() {
      eventService.removeSubscriber(this);
      threads.values().forEach(ISubmittedLongLivingTask::cancel);
      threads.clear();
      setStatus(ServiceStatus.DEACTIVATED);
      logService.info(getClass(), "deactivated");
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

   private void publishData1(Data1 output1) {
      Preconditions.checkNotNull(output1, "output1 may not be null!");
      eventService.publish(output1, Data1.TOPIC);
   }

   private void publishData5(Data5 output3) {
      Preconditions.checkNotNull(output3, "output3 may not be null!");
      eventService.publish(output3, Data5.TOPIC);
   }

   private void publishData4(Data4 output2) {
      Preconditions.checkNotNull(output2, "output2 may not be null!");
      eventService.publish(output2, Data4.TOPIC);
   }

}
