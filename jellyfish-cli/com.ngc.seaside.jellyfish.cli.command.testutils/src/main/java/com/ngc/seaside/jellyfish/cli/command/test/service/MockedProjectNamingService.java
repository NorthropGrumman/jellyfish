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

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Objects;

/**
 * A basic implementation of {@link IProjectNamingService} for tests
 */
public class MockedProjectNamingService implements IProjectNamingService {

   private final String projectFormat = "%s.%s.%s";
   private final String gavFormat = "%s:%s.%s:%2$sVersion";

   private static class ProjectInformation implements IProjectInformation {

      private String project;
      private String group;
      private String artifact;
      private String version;

      @Override
      public int hashCode() {
         return Objects.hash(getDirectoryName(),
                             getGroupId(),
                             getArtifactId(),
                             getVersionPropertyName(),
                             getGavFormattedString());
      }

      @Override
      public boolean equals(Object obj) {
         if (!(obj instanceof IProjectInformation)) {
            return false;
         }
         IProjectInformation that = (IProjectInformation) obj;
         return Objects.equals(this.getDirectoryName(), that.getDirectoryName())
                && Objects.equals(this.getGroupId(), that.getGroupId())
                && Objects.equals(this.getArtifactId(), that.getArtifactId())
                && Objects.equals(this.getVersionPropertyName(), that.getVersionPropertyName())
                && Objects.equals(this.getGavFormattedString(), that.getGavFormattedString());
      }

      public ProjectInformation(String project, String gav) {
         this.project = project;
         String[] parts = gav.split(":");
         group = parts[0];
         artifact = parts[1];
         version = parts[2];
      }

      @Override
      public String getDirectoryName() {
         return project;
      }

      @Override
      public String getArtifactId() {
         return artifact;
      }

      @Override
      public String getGroupId() {
         return group;
      }

      @Override
      public String getVersionPropertyName() {
         return version;
      }

      @Override
      public String getGavFormattedString() {
         return group + ":" + artifact + ":$" + version;
      }

   }

   @Override
   public String getRootProjectName(IJellyFishCommandOptions options, IModel model) {
      return model.getFullyQualifiedName().toLowerCase();
   }

   private IProjectInformation getProjectName(IModel model, String projectType) {
      String pkg = model.getParent().getName().toLowerCase();
      String name = model.getName().toLowerCase();
      return new ProjectInformation(String.format(projectFormat, pkg, name, projectType),
                                    String.format(gavFormat, pkg, name, projectType));
   }

   @Override
   public IProjectInformation getDomainProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "domain");
   }

   @Override
   public IProjectInformation getEventsProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "event");
   }

   @Override
   public IProjectInformation getConnectorProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "connector");
   }

   @Override
   public IProjectInformation getServiceProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "impl");
   }

   @Override
   public IProjectInformation getBaseServiceProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "base");
   }

   @Override
   public IProjectInformation getMessageProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "message");
   }

   @Override
   public IProjectInformation getDistributionProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "dist");
   }

   @Override
   public IProjectInformation getCucumberTestsProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "tests");
   }

   @Override
   public IProjectInformation getConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "config");
   }

   @Override
   public IProjectInformation getGeneratedConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "config");
   }

   @Override
   public IProjectInformation getCucumberTestsConfigProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "testsconfig");
   }

   @Override
   public IProjectInformation getPubSubBridgeProjectName(IJellyFishCommandOptions options, IModel model) {
      return getProjectName(model, "pubsubbridge");
   }

}
