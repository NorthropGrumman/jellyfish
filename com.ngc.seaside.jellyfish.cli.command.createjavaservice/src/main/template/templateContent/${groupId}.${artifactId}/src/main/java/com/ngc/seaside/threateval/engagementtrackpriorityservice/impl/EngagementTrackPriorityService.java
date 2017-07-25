package com.ngc.seaside.threateval.engagementtrackpriorityservice.impl;

import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.service.fault.api.ServiceInputFaultException;
import com.ngc.seaside.threateval.engagementplanning.events.TrackEngagementStatus;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.api.IEngagementTrackPriorityService;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl.AbstractEngagementTrackPriorityService;
import com.ngc.seaside.threateval.events.TrackPriority;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {IEngagementTrackPriorityService.class, IServiceModule.class}, immediate = true)
public class EngagementTrackPriorityService extends AbstractEngagementTrackPriorityService {

   @Override
   public TrackPriority calculateTrackPriority(TrackEngagementStatus trackEngagementStatus)
         throws ServiceFaultException {
      logService.debug(getClass(), "Computing track priority.");
      if (trackEngagementStatus.getProbabilityOfKill() < 0 || trackEngagementStatus.getProbabilityOfKill() > 1) {
         throw new ServiceInputFaultException(
               this,
               trackEngagementStatus,
               String.format("got an invalid probabilityOfKill %f for track %d in an TrackEngagementStatus event!",
                             trackEngagementStatus.getProbabilityOfKill(),
                             trackEngagementStatus.getTrackId()));
      }

      logService.debug(getClass(), "Track priority computed.");
      return new TrackPriority()
            .setTrackId(trackEngagementStatus.getTrackId())
            .setPriority(1.0f - trackEngagementStatus.getProbabilityOfKill())
            .setSourceId(NAME);
   }

   @Activate
   public void activate() {
      super.activate();
   }

   @Deactivate
   public void deactivate() {
      super.deactivate();
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Override
   public void removeLogService(ILogService ref) {
      super.removeLogService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setEventService(IEventService ref) {
      super.setEventService(ref);
   }

   @Override
   public void removeEventService(IEventService ref) {
      super.removeEventService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setFaultManagementService(IFaultManagementService ref) {
      super.setFaultManagementService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void removeFaultManagementService(IFaultManagementService ref) {
      super.removeFaultManagementService(ref);
   }
}
