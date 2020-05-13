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
