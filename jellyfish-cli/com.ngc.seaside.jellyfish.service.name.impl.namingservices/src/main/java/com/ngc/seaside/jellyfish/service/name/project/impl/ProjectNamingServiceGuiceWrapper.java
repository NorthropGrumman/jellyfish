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
package com.ngc.seaside.jellyfish.service.name.project.impl;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class ProjectNamingServiceGuiceWrapper implements IProjectNamingService {

   private final ProjectNamingService projectNamingService;

   @Inject
   public ProjectNamingServiceGuiceWrapper(ILogService logService) {
      projectNamingService = new ProjectNamingService();
      projectNamingService.setLogService(logService);
      projectNamingService.activate();
   }

   @Override
   public String getRootProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getRootProjectName(options, model);
   }

   @Override
   public IProjectInformation getDomainProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getDomainProjectName(options, model);
   }

   @Override
   public IProjectInformation getEventsProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getEventsProjectName(options, model);
   }

   @Override
   public IProjectInformation getMessageProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getMessageProjectName(options, model);
   }

   @Override
   public IProjectInformation getConnectorProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getConnectorProjectName(options, model);
   }

   @Override
   public IProjectInformation getServiceProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getServiceProjectName(options, model);
   }

   @Override
   public IProjectInformation getBaseServiceProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getBaseServiceProjectName(options, model);
   }

   @Override
   public IProjectInformation getDistributionProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getDistributionProjectName(options, model);
   }

   @Override
   public IProjectInformation getCucumberTestsProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getCucumberTestsProjectName(options, model);
   }

   @Override
   public IProjectInformation getConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getConfigProjectName(options, model);
   }

   @Override
   public IProjectInformation getGeneratedConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getGeneratedConfigProjectName(options, model);
   }

   @Override
   public IProjectInformation getCucumberTestsConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getCucumberTestsConfigProjectName(options, model);
   }

   @Override
   public IProjectInformation getPubSubBridgeProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getPubSubBridgeProjectName(options, model);
   }
}
