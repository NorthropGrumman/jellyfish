package com.ngc.seaside.jellyfish.service.buildmgmt.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import java.util.Collection;

public interface IBuildManagementService {

   Collection<IBuildDependency> getProjectDependencies(IJellyFishCommandOptions options);

   Collection<IBuildDependency> getBuildDependencies(IJellyFishCommandOptions options);

   IBuildDependency getDependency(IJellyFishCommandOptions options, String groupId, String artifactId);

   IBuildDependency getDependency(IJellyFishCommandOptions options, String groupAndArtifact);
}
