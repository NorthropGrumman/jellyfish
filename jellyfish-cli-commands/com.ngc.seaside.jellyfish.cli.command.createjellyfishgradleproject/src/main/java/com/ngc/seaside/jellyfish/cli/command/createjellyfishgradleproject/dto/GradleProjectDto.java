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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.dto;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GradleProjectDto {

   private String groupId;
   private String projectName;
   private String version;
   private String systemDescriptorGav;
   private String modelName;
   private String deploymentModelName;
   private String gradleDistributionUrl;
   private List<ModelPartDto> modelParts = new ArrayList<>();
   private Collection<IBuildDependency> buildScriptDependencies = new ArrayList<>();
   private SortedMap<String, String> versionProperties = new TreeMap<>();
   private Collection<IProjectInformation> projects = new ArrayList<>();
   private boolean system;

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

   public String getGradleDistributionUrl() {
      return gradleDistributionUrl;
   }

   public GradleProjectDto setGradleDistributionUrl(String gradleDistributionUrl) {
      this.gradleDistributionUrl = gradleDistributionUrl;
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

   public boolean isSystem() {
      return system;
   }

   public GradleProjectDto setSystem(boolean system) {
      this.system = system;
      return this;
   }

   public Collection<ModelPartDto> getModelParts() {
      return modelParts;
   }

   public GradleProjectDto addModelPart(String model, String distribution, String distributionVersion) {
      modelParts.add(new ModelPartDto(model, distribution, distributionVersion));
      return this;
   }

   public static final class ModelPartDto {

      private final String model;
      private final String distribution;
      private final String distributionVersion;

      /**
       * Constructor.
       * 
       * @param model model
       * @param distribution distribution
       * @param distributionVersion distribution version
       */
      public ModelPartDto(String model, String distribution, String distributionVersion) {
         this.model = model;
         this.distribution = distribution;
         this.distributionVersion = distributionVersion;
      }

      public String getModel() {
         return model;
      }

      public String getDistribution() {
         return distribution;
      }

      public String getDistributionVersion() {
         return distributionVersion;
      }
   }
}
