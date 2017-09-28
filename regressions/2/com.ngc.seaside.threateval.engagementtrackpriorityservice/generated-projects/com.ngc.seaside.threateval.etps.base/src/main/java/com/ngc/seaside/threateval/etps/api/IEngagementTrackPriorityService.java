package com.ngc.seaside.threateval.etps.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.etps.event.datatype.TrackPriority;
import com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus;

public interface IEngagementTrackPriorityService {

   TrackPriority calculateTrackPriority(TrackEngagementStatus trackEngagementStatus) throws ServiceFaultException;

}
