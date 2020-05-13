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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class CucumberDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectName;
   private String packageName;
   private String configPackageName;
   private String className;
   private String transportTopicsClass;
   private Set<String> dependencies;
   private Set<String> imports = new LinkedHashSet<String>();
   private Set<String> remoteServices = new LinkedHashSet<>();
   private String configModule;
   private boolean isConfigGenerated;
   private boolean system;
   private ClassDto adviser;
   private ClassDto adviserImpl;

   /**
    * Constructor.
    * 
    * @param buildManagementService build management service
    * @param options jellyfish options
    */
   public CucumberDto(IBuildManagementService buildManagementService,
                      IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
      this.system = CommonParameters.evaluateBooleanParameter(options.getParameters(),
               CommonParameters.SYSTEM.getName(), false);
   }

   public boolean isSystem() {
      return system;
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

   public Set<String> getRemoteServices() {
      return remoteServices;
   }

   public void addRemoteService(String remoteService) {
      this.remoteServices.add(remoteService);
   }

   public ClassDto getAdviser() {
      return adviser;
   }

   public CucumberDto setAdviser(ClassDto adviser) {
      this.adviser = adviser;
      return this;
   }

   public ClassDto getAdviserImpl() {
      return adviserImpl;
   }

   public CucumberDto setAdviserImpl(ClassDto adviserImpl) {
      this.adviserImpl = adviserImpl;
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
