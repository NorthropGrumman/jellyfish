package com.ngc.example.inhertance.databagsservice.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.example.inhertance.databagsservice.event.datatype.B;
import com.ngc.example.inhertance.databagsservice.event.datatype.C;

public interface IDataBagsService {

   C publish(B b) throws ServiceFaultException;

}
