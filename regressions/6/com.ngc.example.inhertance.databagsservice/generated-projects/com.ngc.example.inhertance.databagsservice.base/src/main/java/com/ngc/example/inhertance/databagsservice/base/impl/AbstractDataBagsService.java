package com.ngc.example.inhertance.databagsservice.base.impl;

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
import com.ngc.example.inhertance.databagsservice.api.IDataBagsService;
import com.ngc.example.inhertance.databagsservice.event.datatype.B;
import com.ngc.example.inhertance.databagsservice.event.datatype.C;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;

public abstract class AbstractDataBagsService
   implements IServiceModule, IDataBagsService {

   public final static String NAME = "service:com.ngc.example.inhertance.DataBagsService";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   @Subscriber(B.TOPIC_NAME)
   public void receiveB(IEvent<B> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      B source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doPublish(source);
   }

   private void publishC(C value) {
      Preconditions.checkNotNull(value, "C value may not be null!");
      eventService.publish(value, C.TOPIC);
   }

   private void doPublish(B input) {
      C output;
      try {
         output = publish(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractDataBagsService.publish' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: publish; Input: %s; Output: %s;", input, output);
      publishC(output);
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
