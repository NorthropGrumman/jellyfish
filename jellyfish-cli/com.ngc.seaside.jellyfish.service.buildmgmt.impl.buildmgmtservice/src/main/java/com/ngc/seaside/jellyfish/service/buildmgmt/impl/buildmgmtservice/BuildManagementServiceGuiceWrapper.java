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
package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.util.Collection;

public class BuildManagementServiceGuiceWrapper implements IBuildManagementService {

   private final BuildManagementService buildManagementService;

   @Inject
   public BuildManagementServiceGuiceWrapper(ILogService logService,
                                             DependenciesConfiguration config) {
      buildManagementService = new BuildManagementService();
      buildManagementService.setLogService(logService);
      buildManagementService.setDependenciesConfiguration(config);
      buildManagementService.activate();
   }

   @Override
   public Collection<IBuildDependency> getRegisteredDependencies(ICommandOptions options,
                                                                 DependencyScope type) {
      return buildManagementService.getRegisteredDependencies(options, type);
   }

   @Override
   public IBuildDependency registerDependency(ICommandOptions options, String groupId,
                                              String artifactId) {
      return buildManagementService.registerDependency(options, groupId, artifactId);
   }

   @Override
   public IBuildDependency registerDependency(ICommandOptions options, String groupAndArtifact) {
      return buildManagementService.registerDependency(options, groupAndArtifact);
   }

   @Override
   public IBuildDependency getDependency(
         ICommandOptions options, String groupId, String artifactId) {
      return buildManagementService.getDependency(options, groupId, artifactId);
   }

   @Override
   public IBuildDependency getDependency(
         ICommandOptions options, String groupAndArtifact) {
      return buildManagementService.getDependency(options, groupAndArtifact);
   }

   @Override
   public Collection<IProjectInformation> getRegisteredProjects() {
      return buildManagementService.getRegisteredProjects();
   }

   @Override
   public void registerProject(ICommandOptions options, IProjectInformation project) {
      buildManagementService.registerProject(options, project);
   }
}
