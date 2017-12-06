package com.ngc.seaside.threateval.datps.base.impl;

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
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.datps.api.IDefendedAreaTrackPriorityService;
import com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack;
import com.ngc.seaside.threateval.datps.event.datatype.TrackPriority;

public abstract class AbstractDefendedAreaTrackPriorityService
   implements IServiceModule, IDefendedAreaTrackPriorityService {

   public final static String NAME = "service:com.ngc.seaside.threateval.DefendedAreaTrackPriorityService";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   @Subscriber(SystemTrack.TOPIC_NAME)
   public void receiveSystemTrack(IEvent<SystemTrack> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      SystemTrack source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doCalculateTrackPriority(source);
   }

   private void publishTrackPriority(TrackPriority value) {
      Preconditions.checkNotNull(value, "TrackPriority value may not be null!");
      eventService.publish(value, TrackPriority.TOPIC);
   }

   private void doCalculateTrackPriority(SystemTrack input) {
      TrackPriority output;
      try {
         output = calculateTrackPriority(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractDefendedAreaTrackPriorityService.calculateTrackPriority' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: calculateTrackPriority; Input: %s; Output: %s;", input, output);
      publishTrackPriority(output);
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
