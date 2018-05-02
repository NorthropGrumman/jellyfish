package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;



public class PubSubBridgeDto {
   
   private String projectName;
   private String className;
   private String packageName;
   
   public String getProjectName() {
      return projectName;
   }
   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }
   public String getClassName() {
      return className;
   }
   public void setClassName(String className) {
      this.className = className;
   }
   public void setPackageName(String packageName) {
      this.packageName = packageName;  
   }
   public String getPackageName() {
      return packageName;
   }
}
