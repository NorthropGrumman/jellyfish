package com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Set;

public class ConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private IModel model;
   private String packageName;
   private String projectName;
   private Set<String> projectDependencies;

   public ConfigDto(IBuildManagementService buildManagementService,
                    IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public IModel getModel() {
      return model;
   }

   public ConfigDto setModel(IModel model) {
      this.model = model;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ConfigDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }
   
   public String getProjectName() {
      return projectName;
   }
   
   public ConfigDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }
   
   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public void setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
   }

   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
