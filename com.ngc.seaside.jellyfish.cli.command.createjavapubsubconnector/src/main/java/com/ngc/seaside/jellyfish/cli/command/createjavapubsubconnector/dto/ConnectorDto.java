package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector.dto;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Map;
import java.util.Set;

public class ConnectorDto {
   private IModel model;
   private String packageName;
   private String basePackage;
   private String projectName;
   private String artifactId;
   private String groupId;
   private String baseArtifactId;
   private Set<IData> allInputData;
   private Set<IEnumeration> allInputEnums;
   private Set<IData> allOutputData;
   private Set<IEnumeration> allOutputEnums;
   private Map<String, IData> inputTopics;
   private Map<String, IData> outputTopics;
   private Map<String, Set<String>> topicRequirements;

   public IModel getModel() {
      return model;
   }

   public ConnectorDto setModel(IModel model) {
      this.model = model;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ConnectorDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getBasePackage() {
      return basePackage;
   }

   public ConnectorDto setBasePackage(String basePackage) {
      this.basePackage = basePackage;
      return this;
   }

   public String getProjectName() {
      return projectName;
   }

   public ConnectorDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public String getArtifactId() {
      return artifactId;
   }

   public ConnectorDto setArtifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
   }

   public String getGroupId() {
      return groupId;
   }

   public ConnectorDto setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public String getBaseArtifactId() {
      return baseArtifactId;
   }

   public ConnectorDto setBaseArtifactId(String baseArtifactId) {
      this.baseArtifactId = baseArtifactId;
      return this;
   }

   public Set<IData> getAllInputData() {
      return allInputData;
   }

   public ConnectorDto setAllInputData(Set<IData> allInputData) {
      this.allInputData = allInputData;
      return this;
   }

   public Set<IEnumeration> getAllInputEnums() {
      return allInputEnums;
   }

   public ConnectorDto setAllInputEnums(Set<IEnumeration> allInputEnums) {
      this.allInputEnums = allInputEnums;
      return this;
   }

   public Set<IData> getAllOutputData() {
      return allOutputData;
   }

   public ConnectorDto setAllOutputData(Set<IData> allOutputData) {
      this.allOutputData = allOutputData;
      return this;
   }

   public Set<IEnumeration> getAllOutputEnums() {
      return allOutputEnums;
   }

   public ConnectorDto setAllOutputEnums(Set<IEnumeration> allOutputEnums) {
      this.allOutputEnums = allOutputEnums;
      return this;
   }

   public Map<String, IData> getInputTopics() {
      return inputTopics;
   }

   public ConnectorDto setInputTopics(Map<String, IData> inputTopics) {
      this.inputTopics = inputTopics;
      return this;
   }

   public Map<String, IData> getOutputTopics() {
      return outputTopics;
   }

   public ConnectorDto setOutputTopics(Map<String, IData> outputTopics) {
      this.outputTopics = outputTopics;
      return this;
   }

   public Map<String, Set<String>> getTopicRequirements() {
      return topicRequirements;
   }

   public ConnectorDto setTopicRequirements(Map<String, Set<String>> topicRequirements) {
      this.topicRequirements = topicRequirements;
      return this;
   }
}
