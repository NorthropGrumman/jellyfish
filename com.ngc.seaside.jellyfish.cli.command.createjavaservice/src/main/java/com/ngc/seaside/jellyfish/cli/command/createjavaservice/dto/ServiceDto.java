package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.Set;

public class ServiceDto {

   private String projectDirectoryName;
   private ClassDto service;
   private Set<String> projectDependencies;
   private String interfaze;
   private String baseClass;
   
   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public ServiceDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public ClassDto getService() {
      return service;
   }

   public ServiceDto setService(ClassDto service) {
      this.service = service;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public ServiceDto setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
      return this;
   }

   public String getInterface() {
      return interfaze;
   }

   public ServiceDto setInterface(String interfaze) {
      this.interfaze = interfaze;
      return this;
   }

   public String getBaseClass() {
      return baseClass;
   }

   public ServiceDto setBaseClass(String baseClass) {
      this.baseClass = baseClass;
      return this;
   }
   

}
