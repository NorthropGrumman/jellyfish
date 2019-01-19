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
package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
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
   public Collection<IBuildDependency> getRegisteredDependencies(IJellyFishCommandOptions options,
                                                                 DependencyScope type) {
      return buildManagementService.getRegisteredDependencies(options, type);
   }

   @Override
   public IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupId,
                                              String artifactId) {
      return buildManagementService.registerDependency(options, groupId, artifactId);
   }

   @Override
   public IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupAndArtifact) {
      return buildManagementService.registerDependency(options, groupAndArtifact);
   }

   @Override
   public IBuildDependency getDependency(
         IJellyFishCommandOptions options, String groupId, String artifactId) {
      return buildManagementService.getDependency(options, groupId, artifactId);
   }

   @Override
   public IBuildDependency getDependency(
         IJellyFishCommandOptions options, String groupAndArtifact) {
      return buildManagementService.getDependency(options, groupAndArtifact);
   }

   @Override
   public Collection<IProjectInformation> getRegisteredProjects() {
      return buildManagementService.getRegisteredProjects();
   }

   @Override
   public void registerProject(IJellyFishCommandOptions options, IProjectInformation project) {
      buildManagementService.registerProject(options, project);
   }
}
