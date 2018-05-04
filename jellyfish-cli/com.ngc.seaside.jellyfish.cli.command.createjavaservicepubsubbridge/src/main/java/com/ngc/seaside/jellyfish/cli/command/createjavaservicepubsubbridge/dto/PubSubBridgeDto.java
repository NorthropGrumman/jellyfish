package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.Set;
import java.util.TreeSet;

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
   
   public String getSubscriberDataType() {
      return subscriberDataType;
   }
   
   public PubSubBridgeDto setSubscriberDataType(String subscriberDataType) {
      this.subscriberDataType = subscriberDataType;
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
