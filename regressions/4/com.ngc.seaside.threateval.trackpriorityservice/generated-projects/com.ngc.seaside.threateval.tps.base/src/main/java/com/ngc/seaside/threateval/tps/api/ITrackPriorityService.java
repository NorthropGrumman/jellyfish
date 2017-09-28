package com.ngc.seaside.threateval.tps.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack;
import com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers;
import com.ngc.seaside.threateval.tps.event.datatype.TrackPriority;

public interface ITrackPriorityService {

   PrioritizedSystemTrackIdentifiers calculateConsolidatedTrackPriority(TrackPriority trackPriority) throws ServiceFaultException;

   PrioritizedSystemTrackIdentifiers calculateConsolidatedTrackPriorityWhenTrackDropped(DroppedSystemTrack droppedSystemTrack) throws ServiceFaultException;

}
