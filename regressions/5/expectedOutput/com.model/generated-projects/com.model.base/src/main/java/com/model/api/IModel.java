package com.model.api;

import com.model.event.Data1;
import com.model.event.Data2;
import com.model.event.Data4;
import com.model.event.Data5;
import com.ngc.seaside.service.correlation.api.ILocalCorrelationEvent;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public interface IModel {

   Data4 basicPubSub(Data1 input1) throws ServiceFaultException;

   void basicSink(Data1 input1) throws ServiceFaultException;

   Data4 singleOutputCorrelation(
      Data1 input1,
      Data2 input2,
      ILocalCorrelationEvent<Integer> correlationEvent) throws ServiceFaultException;

   void multiOutputPubSub(
      BlockingQueue<Data1> input1Queue,
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer);

   void complexPubSub(
      BlockingQueue<Data1> input1Queue,
      BlockingQueue<Data2> input2Queue,
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer);

   void basicSource(
      Consumer<Data4> output1Consumer);

   void multiSource(
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer);

}
