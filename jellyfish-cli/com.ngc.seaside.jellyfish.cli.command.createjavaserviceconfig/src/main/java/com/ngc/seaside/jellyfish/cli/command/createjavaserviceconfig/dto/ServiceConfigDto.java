package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

public class ServiceConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String modelName;
   private String packageName;
   private String projectDirectoryName;
   private String baseProjectArtifactName;

   public ServiceConfigDto(IBuildManagementService buildManagementService,
                           IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getModelName() {
      return modelName;
   }

   public ServiceConfigDto setModelName(String modelName) {
      this.modelName = modelName;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ServiceConfigDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getBaseProjectArtifactName() {
      return baseProjectArtifactName;
   }

   public ServiceConfigDto setBaseProjectArtifactName(String baseProjectArtifactName) {
      this.baseProjectArtifactName = baseProjectArtifactName;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public ServiceConfigDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
