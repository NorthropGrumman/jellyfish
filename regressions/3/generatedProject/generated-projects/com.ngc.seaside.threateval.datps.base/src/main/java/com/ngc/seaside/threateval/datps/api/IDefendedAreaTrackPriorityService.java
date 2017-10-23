package com.ngc.seaside.threateval.datps.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.datps.event.atype.TrackPriority;
import com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack;

public interface IDefendedAreaTrackPriorityService {

   TrackPriority calculateTrackPriority(SystemTrack systemTrack) throws ServiceFaultException;

}
