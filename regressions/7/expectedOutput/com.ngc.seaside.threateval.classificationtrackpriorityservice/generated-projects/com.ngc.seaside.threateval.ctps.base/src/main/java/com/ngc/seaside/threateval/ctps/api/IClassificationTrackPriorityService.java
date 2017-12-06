package com.ngc.seaside.threateval.ctps.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.threateval.ctps.event.classifier.datatype.Classification;
import com.ngc.seaside.threateval.ctps.event.datatype.TrackPriority;

public interface IClassificationTrackPriorityService {

   TrackPriority calculateTrackPriority(Classification systemTrackClassification) throws ServiceFaultException;

}
