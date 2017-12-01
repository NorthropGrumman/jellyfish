package com.ngc.seaside.threateval.tps.base.impl;

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
import com.ngc.seaside.threateval.tps.api.ITrackPriorityService;
import com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack;
import com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers;
import com.ngc.seaside.threateval.tps.event.datatype.TrackPriority;

public abstract class AbstractTrackPriorityService
   implements IServiceModule, ITrackPriorityService {

   public final static String NAME = "service:com.ngc.seaside.threateval.TrackPriorityService";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

   @Subscriber(TrackPriority.TOPIC_NAME)
   public void receiveTrackPriority(IEvent<TrackPriority> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      TrackPriority source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doCalculateConsolidatedTrackPriority(source);
   }

   @Subscriber(DroppedSystemTrack.TOPIC_NAME)
   public void receiveDroppedSystemTrack(IEvent<DroppedSystemTrack> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      DroppedSystemTrack source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

      doCalculateConsolidatedTrackPriorityWhenTrackDropped(source);
   }

   private void publishPrioritizedSystemTrackIdentifiers(PrioritizedSystemTrackIdentifiers value) {
      Preconditions.checkNotNull(value, "PrioritizedSystemTrackIdentifiers value may not be null!");
      eventService.publish(value, PrioritizedSystemTrackIdentifiers.TOPIC);
   }

   private void doCalculateConsolidatedTrackPriority(TrackPriority input) {
      PrioritizedSystemTrackIdentifiers output;
      try {
         output = calculateConsolidatedTrackPriority(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractTrackPriorityService.calculateConsolidatedTrackPriority' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: calculateConsolidatedTrackPriority; Input: %s; Output: %s;", input, output);
      publishPrioritizedSystemTrackIdentifiers(output);
   }

   private void doCalculateConsolidatedTrackPriorityWhenTrackDropped(DroppedSystemTrack input) {
      PrioritizedSystemTrackIdentifiers output;
      try {
         output = calculateConsolidatedTrackPriorityWhenTrackDropped(input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of 'AbstractTrackPriorityService.calculateConsolidatedTrackPriorityWhenTrackDropped' generated a fault, dispatching to fault management service.");
         return;
      }
      logService.info(getClass(), "ELK - Scenario: calculateConsolidatedTrackPriorityWhenTrackDropped; Input: %s; Output: %s;", input, output);
      publishPrioritizedSystemTrackIdentifiers(output);
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
