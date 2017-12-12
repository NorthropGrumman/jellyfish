package com.ngc.seaside.jellyfish.examples.project1.model.api;

import com.ngc.seaside.jellyfish.examples.project1.model.event.Data1;
import com.ngc.seaside.jellyfish.examples.project1.model.event.common.datatype.GPSTime;
import com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2;
import com.ngc.seaside.service.fault.api.ServiceFaultException;

public interface IModel {

   Data1 scenario1(Data2 input1) throws ServiceFaultException;

   Data1 scenario2(GPSTime input2) throws ServiceFaultException;

}
