package com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Set;

public class ConfigDto {
   private IModel model;
   private String packageName;
   private String projectName;
   private Set<String> projectDependencies;

   public IModel getModel() {
      return model;
   }

   public ConfigDto setModel(IModel model) {
      this.model = model;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ConfigDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }
   
   public String getProjectName() {
      return projectName;
   }
   
   public ConfigDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }
   
   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public void setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
   }
}
