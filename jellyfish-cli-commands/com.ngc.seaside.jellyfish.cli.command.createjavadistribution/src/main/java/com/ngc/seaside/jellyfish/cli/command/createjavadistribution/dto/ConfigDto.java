package com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Map;

public class ConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private IModel model;
   private String packageName;
   private String projectName;
   private Map<String, String> projectDependencies;

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

   public Map<String, String> getProjectDependencies() {
      return projectDependencies;
   }

   public void setProjectDependencies(Map<String, String> projectDependencies) {
      this.projectDependencies = projectDependencies;
   }

   /**
    *
    * @param groupAndArtifactId String of the group ID that you want formatted
    * @return String of formatted dependency
    */
   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
