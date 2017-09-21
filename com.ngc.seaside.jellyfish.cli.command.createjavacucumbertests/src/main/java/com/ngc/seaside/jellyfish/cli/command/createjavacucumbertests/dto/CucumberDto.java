package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

import java.util.Set;

public class CucumberDto {

   private String projectName;
   private String packageName;
   private String className;
   private String transportTopicsClass;
   private Set<String> dependencies;

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
   
}
