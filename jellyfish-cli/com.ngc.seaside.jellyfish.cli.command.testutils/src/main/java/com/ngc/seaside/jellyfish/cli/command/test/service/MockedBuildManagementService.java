/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

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
   public void registerProject(IJellyFishCommandOptions options, IProjectInformation project) {
   }
}
