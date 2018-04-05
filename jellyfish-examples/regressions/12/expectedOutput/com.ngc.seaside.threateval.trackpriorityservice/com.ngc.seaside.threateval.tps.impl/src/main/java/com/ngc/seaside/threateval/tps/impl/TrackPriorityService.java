package com.ngc.seaside.threateval.tps.impl;

import com.ngc.seaside.threateval.tps.api.ITrackPriorityService;
import com.ngc.seaside.threateval.tps.base.impl.AbstractTrackPriorityService;
import com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack;
import com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers;
import com.ngc.seaside.threateval.tps.event.datatype.TrackPriority;
import com.ngc.seaside.threateval.tps.event.datatype.TrackPriorityRequest;
import com.ngc.seaside.threateval.tps.event.datatype.TrackPriorityResponse;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {ITrackPriorityService.class, IServiceModule.class}, immediate = true)
public class TrackPriorityService extends AbstractTrackPriorityService {

   @Override
   public PrioritizedSystemTrackIdentifiers calculateConsolidatedTrackPriority(TrackPriority trackPriority) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public PrioritizedSystemTrackIdentifiers calculateConsolidatedTrackPriorityWhenTrackDropped(DroppedSystemTrack droppedSystemTrack) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public TrackPriorityResponse getTrackPriorities(TrackPriorityRequest trackPriorityRequest) throws ServiceFaultException{
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Override
   public void removeLogService(ILogService ref) {
      super.removeLogService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeEventService")
   public void setEventService(IEventService ref) {
      super.setEventService(ref);
   }

   @Override
   public void removeEventService(IEventService ref) {
      super.removeEventService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeFaultManagementService")
   public void setFaultManagementService(IFaultManagementService ref) {
      super.setFaultManagementService(ref);
   }

   @Override
   public void removeFaultManagementService(IFaultManagementService ref) {
      super.removeFaultManagementService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeThreadService")
   public void setThreadService(IThreadService ref) {
      super.setThreadService(ref);
   }

   @Override
   public void removeThreadService(IThreadService ref) {
      super.removeThreadService(ref);
   }

}
