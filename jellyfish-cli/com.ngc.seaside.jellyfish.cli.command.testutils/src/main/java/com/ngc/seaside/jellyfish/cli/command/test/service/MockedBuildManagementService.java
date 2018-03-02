package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.nio.file.Path;
import java.util.Collection;

public class MockedBuildManagementService implements IBuildManagementService {

   @Override
   public Collection<IBuildDependency> getRegisteredDependencies(IJellyFishCommandOptions options,
                                                                 DependencyScope type) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupId, String artifactId) {
      return new IBuildDependency() {
         @Override
         public String getGroupId() {
            return groupId;
         }

         @Override
         public String getArtifactId() {
            return artifactId;
         }

         @Override
         public String getVersion() {
            return "1.0";
         }

         @Override
         public String getVersionPropertyName() {
            return "version";
         }
      };
   }

   @Override
   public IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupAndArtifact) {
      String[] parts = groupAndArtifact.split(":");
      return registerDependency(options, parts[0], parts[1]);
   }

   @Override
   public IBuildDependency getDependency(IJellyFishCommandOptions options, String groupId, String artifactId) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency getDependency(IJellyFishCommandOptions options, String groupAndArtifact) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Collection<IProjectInformation> getRegisteredProjects() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void registerProject(IProjectInformation project) {
   }
}
