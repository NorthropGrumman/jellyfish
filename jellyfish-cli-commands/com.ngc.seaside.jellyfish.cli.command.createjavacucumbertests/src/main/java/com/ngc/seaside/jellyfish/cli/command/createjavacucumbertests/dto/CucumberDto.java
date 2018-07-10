package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

import java.util.HashSet;
import java.util.Set;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

public class CucumberDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectName;
   private String packageName;
   private String configPackageName;
   private String className;
   private String transportTopicsClass;
   private Set<String> dependencies;
   private Set<String> imports = new HashSet<String>();;
   private String configModule;
   private boolean isConfigGenerated;


   public CucumberDto(IBuildManagementService buildManagementService,
                      IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getProjectName() {
      return projectName;
   }

   public CucumberDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }
   
   public String getPackageName() {
      return packageName;
   }

   public CucumberDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getConfigPackageName() {
      return configPackageName;
   }

   public CucumberDto setConfigPackageName(String configPackageName) {
      this.configPackageName = configPackageName;
      return this;
   }

   public String getClassName() {
      return className;
   }

   public CucumberDto setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getTransportTopicsClass() {
      return transportTopicsClass;
   }

   public CucumberDto setTransportTopicsClass(String transportTopicsClass) {
      this.transportTopicsClass = transportTopicsClass;
      return this;
   }

   public Set<String> getDependencies() {
      return dependencies;
   }

   public CucumberDto setDependencies(Set<String> dependencies) {
      this.dependencies = dependencies;
      return this;
   }
   
   public Set<String> getImports() {
      return imports;
   }

   public CucumberDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getConfigModule() {
      return configModule;
   }

   public CucumberDto setConfigModule(String configModule) {
      this.configModule = configModule;
      return this;
   }

   public String getConfigModuleType() {
      return configModule.substring(configModule.lastIndexOf('.') + 1);
   }

   public String getConfigModulePackage() {
      return configModule.substring(0, configModule.lastIndexOf('.'));
   }

   public boolean isConfigGenerated() {
      return isConfigGenerated;
   }

   public CucumberDto setConfigGenerated(boolean isConfigGenerated) {
      this.isConfigGenerated = isConfigGenerated;
      return this;
   }

   /**
    *
    * @param groupAndArtifactId String of the group Id that you want the formatted string for
    * @return a formatted String of the dependency
    */
   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
