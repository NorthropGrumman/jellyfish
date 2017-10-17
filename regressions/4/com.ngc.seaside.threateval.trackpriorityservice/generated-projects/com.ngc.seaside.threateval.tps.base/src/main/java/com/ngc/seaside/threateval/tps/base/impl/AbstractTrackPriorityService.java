package com.ngc.seaside.threateval.tps.base.impl;

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

   protected Map<String, ISubmittedLongLivingTask> threads = new ConcurrentHashMap<>();

   @Subscriber(DroppedSystemTrack.TOPIC_NAME)
   public void receiveDroppedSystemTrack(IEvent<DroppedSystemTrack> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         publishPrioritizedSystemTrackIdentifiers(calculateConsolidatedTrackPriorityWhenTrackDropped(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.calculateConsolidatedTrackPriorityWhenTrackDropped(DroppedSystemTrack)' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

   }

   @Subscriber(TrackPriority.TOPIC_NAME)
   public void receiveTrackPriority(IEvent<TrackPriority> event) {
      Preconditions.checkNotNull(event, "event may not be null!");

      try {
         publishPrioritizedSystemTrackIdentifiers(calculateConsolidatedTrackPriority(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.calculateConsolidatedTrackPriority(TrackPriority)' generated fault, dispatching to fault management service.",
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

   private void publishPrioritizedSystemTrackIdentifiers(PrioritizedSystemTrackIdentifiers prioritizedSystemTracks) {
      Preconditions.checkNotNull(prioritizedSystemTracks, "prioritizedSystemTracks may not be null!");
      eventService.publish(prioritizedSystemTracks, PrioritizedSystemTrackIdentifiers.TOPIC);
   }

}
