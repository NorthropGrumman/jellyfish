package com.model.impl;

import com.model.api.IModel;
import com.model.base.impl.AbstractModel;
import com.model.event.Data1;
import com.model.event.Data2;
import com.model.event.Data4;
import com.model.event.Data5;
import com.ngc.seaside.service.correlation.api.ILocalCorrelationEvent;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.service.correlation.api.ICorrelationService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {IModel.class, IServiceModule.class}, immediate = true)
public class Model extends AbstractModel {

   @Override
   public Data4 basicPubSub(Data1 input1) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void basicSink(Data1 input1) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Data4 singleOutputCorrelation(
      Data1 input1,
      Data2 input2,
      ILocalCorrelationEvent<Integer> correlationEvent) throws ServiceFaultException {
         // TODO: implement this
         throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void multiOutputPubSub(
      BlockingQueue<Data1> input1Queue,
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void complexPubSub(
      BlockingQueue<Data1> input1Queue,
      BlockingQueue<Data2> input2Queue,
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void basicSource(
      Consumer<Data4> output1Consumer) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void multiSource(
      Consumer<Data4> output1Consumer,
      Consumer<Data5> output2Consumer) {
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

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeCorrelationService")
   public void setCorrelationService(ICorrelationService ref) {
      this.correlationService = ref;
   }

   @Override
   public void removeCorrelationService(ICorrelationService ref) {
      setCorrelationService(null);
   }

}
