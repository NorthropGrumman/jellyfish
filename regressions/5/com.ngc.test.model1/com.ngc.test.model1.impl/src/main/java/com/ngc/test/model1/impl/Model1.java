package com.ngc.test.model1.impl;

import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.blocs.service.thread.api.IThreadService;

import com.ngc.test.model1.api.IModel1;
import com.ngc.test.model1.base.impl.AbstractModel1;
import com.ngc.test.model1.event.Data1;
import com.ngc.test.model1.event.Data2;
import com.ngc.test.model1.event.Data5;
import com.ngc.test.model1.event.test2.Data3;
import com.ngc.test.model1.event.test2.Data4;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {IModel1.class, IServiceModule.class}, immediate = true)
public class Model1 extends AbstractModel1 {

   @Override
   public Data1 pubsub1(Data1 input1) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Data1 duplicatePubsub(Data1 input1) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Data4 pubsub2(Data2 input2) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void sink1(Data1 input1) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void duplicateSink(Data1 input1) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void sink2(Data3 input3) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void source1(Consumer<Data1> consumer) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void duplicateSource(Consumer<Data1> consumer) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void source2(Consumer<Data5> consumer) {
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
