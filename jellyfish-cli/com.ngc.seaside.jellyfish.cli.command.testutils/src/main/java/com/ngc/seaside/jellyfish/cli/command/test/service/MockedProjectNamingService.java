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
         return Objects.equals(this.getDirectoryName(), that.getDirectoryName()) &&
            Objects.equals(this.getGroupId(), that.getGroupId()) &&
            Objects.equals(this.getArtifactId(), that.getArtifactId()) &&
            Objects.equals(this.getVersionPropertyName(), that.getVersionPropertyName()) &&
            Objects.equals(this.getGavFormattedString(), that.getGavFormattedString());
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

}
