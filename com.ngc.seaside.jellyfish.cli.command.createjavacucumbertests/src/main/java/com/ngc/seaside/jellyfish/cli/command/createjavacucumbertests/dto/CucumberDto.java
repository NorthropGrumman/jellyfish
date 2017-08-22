package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

public class CucumberDto {

   private String className;
   private String packageName;
   private String artifactId;
   private String groupId;
   private String projectDirectoryName;



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

   public CucumberDto setArtifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public CucumberDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;

   }


   public String getClassName() {
      return className;
   }

   public CucumberDto setClassName(String className) {
      this.className = className;
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
