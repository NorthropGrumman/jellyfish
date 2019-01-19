/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.name.packagez.impl;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class PackageNamingServiceGuiceWrapper implements IPackageNamingService {

   private final PackageNamingService packageNamingService;

   @Inject
   public PackageNamingServiceGuiceWrapper(ILogService logService) {
      packageNamingService = new PackageNamingService();
      packageNamingService.setLogService(logService);
      packageNamingService.activate();
   }

   @Override
   public String getDomainPackageName(IJellyFishCommandOptions options,
                                      INamedChild<IPackage> data) {
      return packageNamingService.getDomainPackageName(options, data);
   }

   @Override
   public String getEventPackageName(IJellyFishCommandOptions options,
                                     INamedChild<IPackage> data) {
      return packageNamingService.getEventPackageName(options, data);
   }

   @Override
   public String getMessagePackageName(IJellyFishCommandOptions options,
                                       INamedChild<IPackage> data) {
      return packageNamingService.getMessagePackageName(options, data);
   }

   @Override
   public String getServiceInterfacePackageName(IJellyFishCommandOptions options,
                                                IModel model) {
      return packageNamingService.getServiceInterfacePackageName(options, model);
   }

   @Override
   public String getServiceImplementationPackageName(IJellyFishCommandOptions options,
                                                     IModel model) {
      return packageNamingService.getServiceImplementationPackageName(options, model);
   }

   @Override
   public String getServiceBaseImplementationPackageName(IJellyFishCommandOptions options,
                                                         IModel model) {
      return packageNamingService.getServiceBaseImplementationPackageName(options, model);
   }

   @Override
   public String getTransportTopicsPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getTransportTopicsPackageName(options, model);
   }

   @Override
   public String getConnectorPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getConnectorPackageName(options, model);
   }

   @Override
   public String getDistributionPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getDistributionPackageName(options, model);
   }

   @Override
   public String getCucumberTestsPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getCucumberTestsPackageName(options, model);
   }

   @Override
   public String getCucumberTestsConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getCucumberTestsPackageName(options, model);
   }

   @Override
   public String getConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getConfigPackageName(options, model);
   }

   @Override
   public String getPubSubBridgePackageName(IJellyFishCommandOptions options, IModel model) {
      return packageNamingService.getConfigPackageName(options, model);
   }

}
