/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.name.packagez.impl;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
