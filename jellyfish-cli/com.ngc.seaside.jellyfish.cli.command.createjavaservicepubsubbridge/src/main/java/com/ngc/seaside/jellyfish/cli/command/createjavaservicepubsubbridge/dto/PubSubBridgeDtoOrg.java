package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;


import java.util.ArrayList;
import java.util.List;

public class PubSubBridgeDtoOrg {
   
   private String projectName;
   private String className;
   private String packageName;
   private List<String> imports = new ArrayList<>();;
   
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
   public void setImports(String newImport) { this.imports.add(newImport); }
   public List<String> getImports() { return imports; }
}
