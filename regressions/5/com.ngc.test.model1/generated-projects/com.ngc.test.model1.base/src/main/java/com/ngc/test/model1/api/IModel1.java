package com.ngc.test.model1.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.test.model1.event.Data1;
import com.ngc.test.model1.event.Data2;
import com.ngc.test.model1.event.Data5;
import com.ngc.test.model1.event.test2.Data3;
import com.ngc.test.model1.event.test2.Data4;
import java.util.function.Consumer;

public interface IModel1 {

   Data1 pubsub1(Data1 input1) throws ServiceFaultException;

   Data1 duplicatePubsub(Data1 input1) throws ServiceFaultException;

   Data4 pubsub2(Data2 input2) throws ServiceFaultException;

   void sink1(Data1 input1) throws ServiceFaultException;

   void duplicateSink(Data1 input1) throws ServiceFaultException;

   void sink2(Data3 input3) throws ServiceFaultException;

   void source1(Consumer<Data1> consumer) throws ServiceFaultException;

   void duplicateSource(Consumer<Data1> consumer) throws ServiceFaultException;

   void source2(Consumer<Data5> consumer) throws ServiceFaultException;

}
