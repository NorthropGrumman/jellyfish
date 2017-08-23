package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

public class CucumberDto {

   private String className;
   private String basePackage;
   private String artifactId;
   private String baseArtifactId;
   private String groupId;
   private String projectName;
   private String packageName;

   public String getGroupId() {
      return groupId;
   }

   public CucumberDto setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public String getArtifactId() {
      return artifactId;
   }

   public CucumberDto setArtifactId(String baseArtifactId) {
      this.artifactId = baseArtifactId;
      return this;
   }

   public String getBaseArtifactId() {
      return baseArtifactId;
   }

   public CucumberDto setBaseArtifactId(String baseArtifactId) {
      this.baseArtifactId = baseArtifactId;
      return this;
   }

   public String getProjectName() {
      return projectName;
   }

   public CucumberDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;

   }

   public String getClassName() {
      return className;
   }

   public CucumberDto setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getBasePackage() {
      return basePackage;
   }

   public CucumberDto setBasePackage(String basePackage) {
      this.basePackage = basePackage;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public CucumberDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

}
