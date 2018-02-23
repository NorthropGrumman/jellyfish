package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import java.util.Collection;

public class BuildManagementServiceGuiceWrapper implements IBuildManagementService {

   private final BuildManagementService buildManagementService;

   @Inject
   public BuildManagementServiceGuiceWrapper(ILogService logService,
                                             IResourceService resourceService) {
      buildManagementService = new BuildManagementService();
      buildManagementService.setLogService(logService);
      buildManagementService.setResourceService(resourceService);
      buildManagementService.activate();
   }

   @Override
   public Collection<IBuildDependency> getProjectDependencies(
         IJellyFishCommandOptions options) {
      return buildManagementService.getProjectDependencies(options);
   }

   @Override
   public Collection<IBuildDependency> getBuildDependencies(
         IJellyFishCommandOptions options) {
      return buildManagementService.getBuildDependencies(options);
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
}
