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
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.util.Collection;

public class MockedBuildManagementService implements IBuildManagementService {

   @Override
   public Collection<IBuildDependency> getRegisteredDependencies(ICommandOptions options,
                                                                 DependencyScope type) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency registerDependency(ICommandOptions options, String groupId, String artifactId) {
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
   public IBuildDependency registerDependency(ICommandOptions options, String groupAndArtifact) {
      String[] parts = groupAndArtifact.split(":");
      return registerDependency(options, parts[0], parts[1]);
   }

   @Override
   public IBuildDependency getDependency(ICommandOptions options, String groupId, String artifactId) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency getDependency(ICommandOptions options, String groupAndArtifact) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Collection<IProjectInformation> getRegisteredProjects() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void registerProject(ICommandOptions options, IProjectInformation project) {
   }
}
