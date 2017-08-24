package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnector.dto;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.List;
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
   private List<String> requirements;

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

   public List<String> getRequirements() {
      return requirements;
   }

   public ConnectorDto setRequirements(List<String> requirements) {
      this.requirements = requirements;
      return this;
   }
}
