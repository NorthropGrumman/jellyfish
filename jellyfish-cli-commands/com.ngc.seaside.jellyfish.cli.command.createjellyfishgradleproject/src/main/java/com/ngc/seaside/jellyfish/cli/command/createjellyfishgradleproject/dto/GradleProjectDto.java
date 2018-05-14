package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.dto;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class GradleProjectDto {

   private String groupId;
   private String projectName;
   private String version;
   private String systemDescriptorGav;
   private String modelName;
   private String deploymentModelName;
   private Collection<IBuildDependency> buildScriptDependencies = new ArrayList<>();
   private SortedMap<String, String> versionProperties = new TreeMap<>();
   private Collection<IProjectInformation> projects = new ArrayList<>();

   public String getGroupId() {
      return groupId;
   }

   public GradleProjectDto setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public String getProjectName() {
      return projectName;
   }

   public GradleProjectDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public String getVersion() {
      return version;
   }

   public GradleProjectDto setVersion(String version) {
      this.version = version;
      return this;
   }

   public String getSystemDescriptorGav() {
      return systemDescriptorGav;
   }

   public GradleProjectDto setSystemDescriptorGav(String systemDescriptorGav) {
      this.systemDescriptorGav = systemDescriptorGav;
      return this;
   }

   public String getModelName() {
      return modelName;
   }

   public GradleProjectDto setModelName(String modelName) {
      this.modelName = modelName;
      return this;
   }

   public String getDeploymentModelName() {
      return deploymentModelName;
   }

   public GradleProjectDto setDeploymentModelName(String deploymentModelName) {
      this.deploymentModelName = deploymentModelName;
      return this;
   }

   public Collection<IBuildDependency> getBuildScriptDependencies() {
      return buildScriptDependencies;
   }

   public GradleProjectDto setBuildScriptDependencies(
         Collection<IBuildDependency> buildScriptDependencies) {
      this.buildScriptDependencies = buildScriptDependencies;
      return this;
   }

   public SortedMap<String, String> getVersionProperties() {
      return versionProperties;
   }

   public GradleProjectDto setVersionProperties(SortedMap<String, String> versionProperties) {
      this.versionProperties = versionProperties;
      return this;
   }

   public Collection<IProjectInformation> getProjects() {
      return projects;
   }

   public GradleProjectDto setProjects(
         Collection<IProjectInformation> projects) {
      this.projects = projects;
      return this;
   }
}
