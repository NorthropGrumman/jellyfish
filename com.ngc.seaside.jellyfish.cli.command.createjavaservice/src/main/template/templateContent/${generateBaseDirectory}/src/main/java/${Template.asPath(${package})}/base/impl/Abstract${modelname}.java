package ${package}.base.impl;

import com.google.common.base.Preconditions;

import com.ngc.blocs.api.IContext;
import com.ngc.blocs.api.IStatus;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.api.ServiceStatus;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.Subscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.api.IEngagementTrackPriorityService;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority;

/**
 * Base class for engagement track priority service implementations.
 */
public abstract class Abstract${modelname} implements IServiceModule,
                                                                        IEngagementTrackPriorityService {

   public final static String NAME = "service:${model}";

   /**
    * BLoCS boilerplate.
    */
   protected IContext context;
   /**
    * BLoCS boilerplate.
    */
   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   @Subscriber(TrackEngagementStatus.TOPIC_NAME)
   public void receiveTrackEngagementStatus(IEvent<TrackEngagementStatus> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      try {
         publishTrackPriority(calculateTrackPriority(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(
               getClass(),
               "Invocation of '%s.calculateTrackPriority(TrackEngagementStatus)' generated fault, dispatching to fault"
               + " management service.",
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
   public IContext getContext() {
      return context;
   }

   @Override
   public void setContext(IContext iContext) {
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

   /**
    * Publishes the given {@code TrackPriority} object.
    *
    * @param trackPriority the {@code TrackPriority} to publish
    */
   private void publishTrackPriority(TrackPriority trackPriority) {
      Preconditions.checkNotNull(trackPriority, "trackPriority may not be null!");
      eventService.publish(trackPriority,
                           TrackPriority.TOPIC);
   }
}
