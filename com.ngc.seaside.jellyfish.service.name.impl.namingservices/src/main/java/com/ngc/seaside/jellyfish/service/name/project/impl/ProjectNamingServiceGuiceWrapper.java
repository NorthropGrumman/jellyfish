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
   public IProjectInformation getServiceNoSuffixProjectName(IJellyFishCommandOptions options, IModel model) {
      return projectNamingService.getServiceNoSuffixProjectName(options, model);
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
}
