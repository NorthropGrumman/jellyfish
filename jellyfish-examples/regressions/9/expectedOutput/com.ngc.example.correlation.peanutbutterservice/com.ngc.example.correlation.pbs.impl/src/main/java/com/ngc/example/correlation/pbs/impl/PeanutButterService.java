package com.ngc.example.correlation.pbs.impl;

import com.ngc.example.correlation.pbs.api.IPeanutButterService;
import com.ngc.example.correlation.pbs.base.impl.AbstractPeanutButterService;
import com.ngc.example.correlation.pbs.event.datatype.Butter;
import com.ngc.example.correlation.pbs.event.datatype.Candy;
import com.ngc.example.correlation.pbs.event.datatype.Peanut;
import com.ngc.seaside.service.correlation.api.ILocalCorrelationEvent;
import com.ngc.blocs.service.api.IServiceModule;
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

@Component(service = {IPeanutButterService.class, IServiceModule.class}, immediate = true)
public class PeanutButterService extends AbstractPeanutButterService {

   @Override
   public Candy doCombine(
               Peanut peanut,
               Butter butter) {
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
