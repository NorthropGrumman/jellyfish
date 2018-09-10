/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 * 
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.example.inhertance.databagsservice.impl;

import com.ngc.example.inhertance.databagsservice.api.IDataBagsService;
import com.ngc.example.inhertance.databagsservice.base.impl.AbstractDataBagsService;
import com.ngc.example.inhertance.databagsservice.event.datatype.B;
import com.ngc.example.inhertance.databagsservice.event.datatype.C;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {IDataBagsService.class, IServiceModule.class}, immediate = true)
public class DataBagsService extends AbstractDataBagsService {

   @Override
   public C doPublish(B b) throws ServiceFaultException {
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

}
