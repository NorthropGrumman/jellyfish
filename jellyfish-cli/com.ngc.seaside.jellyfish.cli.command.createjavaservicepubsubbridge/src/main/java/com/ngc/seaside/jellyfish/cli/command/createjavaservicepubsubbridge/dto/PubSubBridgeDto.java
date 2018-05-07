package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class PubSubBridgeDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;
   private static final String SUBSCRIBER_SUFFIX = "Subscriber";

   private String projectName;
   private String packageName;
   private ClassDto service;
   private Set<String> projectDependencies;
   private String interfaze;
   private String baseClass;
   private String subscriberClassName;
   private Set<String> imports = new TreeSet<>();
   private String subscriberDataType;
   private String serviceVarName;
   private String publishDataType;
   private String scenarioMethod;
   private String unbinderSnippet;
   private String binderSnippet;

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
   
   public PubSubBridgeDto setSubscriberClassName(String subscriberClassName) {
      this.subscriberClassName = subscriberClassName + SUBSCRIBER_SUFFIX; 
      return this;
   }
   
   public String getSubscriberClassName() {
      return subscriberClassName;
   }
   
   /**
    * Gets the list of imports needed by this class.
    */
   public Set<String> getImports() {
      return imports;
   }

   public PubSubBridgeDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public List<String> getImportsForDataTypes() {
      List<String> importList = new ArrayList<>();

      imports.stream()
            .filter(importName -> importName.contains(subscriberDataType))
            .forEach(importName -> importList.add(importName));
      return importList;
   }
   
   public String getSubscriberDataType() {
      return subscriberDataType;
   }
   
   public PubSubBridgeDto setSubscriberDataType(String subscriberDataType) {
      this.subscriberDataType = subscriberDataType;
      return this;    
   }
   
   public PubSubBridgeDto setPublishDataType(String publishDataType) {
      this.publishDataType =  publishDataType;
      return this;
   }
   
   public String getPublishDataType() {
      return publishDataType;
   }

   public String getServiceVarName() {
      return serviceVarName;
   }
   
   public PubSubBridgeDto setServiceVarName(String serviceVarName) { 
      String formattedServiceVarName = serviceVarName;
      if(formattedServiceVarName.startsWith("I")) {
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
