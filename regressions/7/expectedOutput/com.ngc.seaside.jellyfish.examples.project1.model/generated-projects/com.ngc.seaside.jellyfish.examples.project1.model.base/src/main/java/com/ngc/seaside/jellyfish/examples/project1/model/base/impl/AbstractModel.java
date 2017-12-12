package com.ngc.seaside.jellyfish.examples.project1.model.base.impl;

import com.google.common.base.Preconditions;
import com.ngc.blocs.api.IContext;
import com.ngc.blocs.api.IStatus;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.api.ServiceStatus;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.Subscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.seaside.jellyfish.examples.project1.model.api.IModel;
import com.ngc.seaside.jellyfish.examples.project1.model.event.Data1;
import com.ngc.seaside.jellyfish.examples.project1.model.event.common.datatype.GPSTime;
import com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;

public abstract class AbstractModel
   implements IServiceModule, IModel {

   public final static String NAME = "service:com.ngc.seaside.jellyfish.examples.project1.Model";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   @Subscriber(Data2.TOPIC_NAME)
   public void receiveData2(IEvent<Data2> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      Data2 source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doScenario1(source);
   }

   @Subscriber(GPSTime.TOPIC_NAME)
   public void receiveGPSTime(IEvent<GPSTime> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      GPSTime source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doScenario2(source);
   }

   private void publishData1(Data1 value) {
      Preconditions.checkNotNull(value, "Data1 value may not be null!");
      eventService.publish(value, Data1.TOPIC);
   }

   private void doScenario1(Data2 input) {
      Data1 output;
      try {
         output = scenario1(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractModel.scenario1' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: scenario1; Input: %s; Output: %s;", input, output);
      publishData1(output);
   }

   private void doScenario2(GPSTime input) {
      Data1 output;
      try {
         output = scenario2(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractModel.scenario2' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: scenario2; Input: %s; Output: %s;", input, output);
      publishData1(output);
   }

   protected void activate() {
      eventService.addSubscriber(this);
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

   protected void deactivate() {
      eventService.removeSubscriber(this);
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
}
