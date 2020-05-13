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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.TreeSet;

public class PubSubBridgeDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;
   private static final String SUBSCRIBER_SUFFIX = "Subscriber";

   private ClassDto service;
   private Set<String> projectDependencies;
   private String interfaze;
   private String baseClass;
   private String abstractClass;
   private String packageName;
   private String projectName;
   private String subscriberClassName;
   private Set<String> imports = new TreeSet<>();
   private String subscriberDataType;
   private String publishDataType;
   private String serviceVarName;
   private String scenarioMethod;
   private String unbinderSnippet;
   private String binderSnippet;
   private boolean correlating;

   /**
    * Default constructor
    *
    * @param buildManagementService the build management service
    * @param options                the jellyfish command options
    */
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

   public String getAbstractClass() {
      return abstractClass;
   }

   public PubSubBridgeDto setAbstractClass(String abstractClass) {
      this.abstractClass = abstractClass;
      return this;
   }


   public String getPackageName() {
      return packageName;
   }

   public PubSubBridgeDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getProjectName() {
      return projectName;
   }

   public PubSubBridgeDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public String getSubscriberClassName() {
      return subscriberClassName;
   }

   public PubSubBridgeDto setSubscriberClassName(String subscriberClassName) {
      this.subscriberClassName = subscriberClassName + SUBSCRIBER_SUFFIX;
      return this;
   }

   public Set<String> getImports() {
      return imports;
   }

   public PubSubBridgeDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getSubscriberDataType() {
      return subscriberDataType;
   }

   public PubSubBridgeDto setSubscriberDataType(String subscriberDataType) {
      this.subscriberDataType = subscriberDataType;
      return this;
   }

   public String getPublishDataType() {
      return publishDataType;
   }

   public PubSubBridgeDto setPublishDataType(String publishDataType) {
      this.publishDataType = publishDataType;
      return this;
   }

   public String getServiceVarName() {
      return serviceVarName;
   }

   public boolean isCorrelating() {
      return correlating;
   }

   public PubSubBridgeDto setCorrelating(boolean correlating) {
      this.correlating = correlating;
      return this;
   }

   /**
    * Reformat the given service name such as "ISomeService"
    * into a variable name such as "someService"
    *
    * @param serviceName the service name
    * @return the formatted service variable name
    */
   public PubSubBridgeDto setServiceVarName(String serviceName) {
      String formattedServiceVarName = serviceName;
      if (formattedServiceVarName.startsWith("I")) {
         formattedServiceVarName = formattedServiceVarName.substring(1);
         formattedServiceVarName = StringUtils.uncapitalize(formattedServiceVarName);
      }
      this.serviceVarName = formattedServiceVarName;
      return this;
   }

   public String getScenarioMethod() {
      return scenarioMethod;
   }

   public PubSubBridgeDto setScenarioMethod(String scenarioMethod) {
      this.scenarioMethod = scenarioMethod;
      return this;
   }

   public String getUnbinderSnippet() {
      return unbinderSnippet;
   }

   public PubSubBridgeDto setUnbinderSnippet(String unbinderSnippet) {
      this.unbinderSnippet = "remove" + StringUtils.capitalize(unbinderSnippet);
      return this;
   }

   public String getBinderSnippet() {
      return binderSnippet;
   }

   public PubSubBridgeDto setBinderSnippet(String binderSnippet) {
      this.binderSnippet = "set" + StringUtils.capitalize(binderSnippet);
      return this;
   }

   /**
    * Formats dependencies for use in a build.gradle file
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
