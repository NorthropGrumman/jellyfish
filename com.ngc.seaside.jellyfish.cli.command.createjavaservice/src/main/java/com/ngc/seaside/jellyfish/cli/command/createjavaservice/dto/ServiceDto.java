package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;

import java.util.Set;

public class ServiceDto {

   private String projectDirectoryName;
   private ClassDto<MethodDto> service;
   private Set<String> projectDependencies;
   

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public ServiceDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public ClassDto<MethodDto> getService() {
      return service;
   }

   public ServiceDto setService(ClassDto<MethodDto> service) {
      this.service = service;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public void setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
   }

}
