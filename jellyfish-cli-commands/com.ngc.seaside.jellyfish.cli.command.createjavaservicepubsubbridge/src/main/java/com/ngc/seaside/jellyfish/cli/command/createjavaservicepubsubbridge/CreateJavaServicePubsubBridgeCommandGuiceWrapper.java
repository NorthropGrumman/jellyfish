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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;

public class CreateJavaServicePubsubBridgeCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServicePubsubBridgeCommand delegate = new CreateJavaServicePubsubBridgeCommand();

   @Inject
   public CreateJavaServicePubsubBridgeCommandGuiceWrapper(ILogService logService,
                                                           IBuildManagementService buildManagementService,
                                                           ITemplateService templateService,
                                                           IBaseServiceDtoFactory templateDtoFactory,
                                                           IJavaServiceGenerationService generateService,
                                                           IPackageNamingService packageNamingService,
                                                           IProjectNamingService projectNamingService,
                                                           IJellyfishUserService jellyfishUserService) {
      delegate.setLogService(logService);
      delegate.setBuildManagementService(buildManagementService);
      delegate.setTemplateService(templateService);
      delegate.setTemplateDaoFactory(templateDtoFactory);
      delegate.setJavaServiceGenerationService(generateService);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setPackageNamingService(packageNamingService);
      delegate.setJellyfishUserService(jellyfishUserService);
   }

   @Override
   public String getName() {
      return delegate.getName();
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }

}
