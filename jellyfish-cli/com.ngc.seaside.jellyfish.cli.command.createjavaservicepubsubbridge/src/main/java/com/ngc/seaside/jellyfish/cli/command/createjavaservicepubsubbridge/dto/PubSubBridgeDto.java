package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.Set;

public class PubSubBridgeDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectName;
   private String packageName;
   private String className;
   private ClassDto service;
   private Set<String> projectDependencies;
   private String interfaze;
   private String baseClass;

   public PubSubBridgeDto(IBuildManagementService buildManagementService,
                     IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public ClassDto getService() {
      return service;
   }

   public PubSubBridgeDto setService(ClassDto service) {
      this.service = service;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public PubSubBridgeDto setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
      return this;
   }

   public String getInterface() {
      return interfaze;
   }

   public PubSubBridgeDto setInterface(String interfaze) {
      this.interfaze = interfaze;
      return this;
   }

   public String getBaseClass() {
      return baseClass;
   }

   public PubSubBridgeDto setBaseClass(String baseClass) {
      this.baseClass = baseClass;
      return this;
   }

   public PubSubBridgeDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }
   public String getPackageName() {
      return packageName;
   }

   public PubSubBridgeDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }
   public String getProjectName() {
      return projectName;
   }

   public PubSubBridgeDto setClassName(String className) {
      this.className = className;
      return this;
   }
   public String getClassName() {
      return className;
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
