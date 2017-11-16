package com.ngc.example.inhertance.databagsservice.base.impl;

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
import com.ngc.example.inhertance.databagsservice.api.IDataBagsService;
import com.ngc.example.inhertance.databagsservice.event.datatype.B;
import com.ngc.example.inhertance.databagsservice.event.datatype.C;

public abstract class AbstractDataBagsService
   implements IServiceModule, IDataBagsService {

   public final static String NAME = "service:com.ngc.example.inhertance.DataBagsService";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   protected Map<String, ISubmittedLongLivingTask> threads = new ConcurrentHashMap<>();

   @Subscriber(B.TOPIC_NAME)
   public void receiveB(IEvent<B> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         publishC(publish(event.getSource()));
         logService.info(getClass(), "ELK - Scenario: %s; Input: %s; Output: %s;", 
        		 "publish", 
        		 event.getSource(), 
        		 publish(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.publish(B)' generated fault, dispatching to fault management service.",
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

   private void publishC(C c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      eventService.publish(c, C.TOPIC);
   }

}
