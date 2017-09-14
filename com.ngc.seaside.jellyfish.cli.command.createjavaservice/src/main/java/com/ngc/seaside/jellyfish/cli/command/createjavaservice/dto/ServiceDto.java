package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import java.util.List;
import java.util.Set;

public class TemplateDto {

   private String className;
   private String packageName;
   private String artifactId;
   private String projectDirectoryName;
   private List<MethodDto> methods;
   private Set<String> imports;
   private ServiceInterfaceDto serviceInterfaceDto;
   private AbstractServiceDto abstractServiceDto;

   public String getClassName() {
      return className;
   }

   public TemplateDto setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public TemplateDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getArtifactId() {
      return artifactId;
   }

   public TemplateDto setArtifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
   }

   public List<MethodDto> getMethods() {
      return methods;
   }

   public TemplateDto setMethods(List<MethodDto> methods) {
      this.methods = methods;
      return this;
   }

   public Set<String> getImports() {
      return imports;
   }

   public TemplateDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public TemplateDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public ServiceInterfaceDto getServiceInterfaceDto() {
      return serviceInterfaceDto;
   }

   public TemplateDto setServiceInterfaceDto(
         ServiceInterfaceDto serviceInterfaceDto) {
      this.serviceInterfaceDto = serviceInterfaceDto;
      return this;
   }

   public AbstractServiceDto getAbstractServiceDto() {
      return abstractServiceDto;
   }

   public TemplateDto setAbstractServiceDto(
         AbstractServiceDto abstractServiceDto) {
      this.abstractServiceDto = abstractServiceDto;
      return this;
   }
}
