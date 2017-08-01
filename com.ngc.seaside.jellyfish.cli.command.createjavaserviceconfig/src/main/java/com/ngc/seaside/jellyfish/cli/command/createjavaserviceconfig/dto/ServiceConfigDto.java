package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig.dto;

public class ServiceConfigDto {

   private String modelName;
   private String packageName;
   private String basePacakgeName;
   private String projectDirectoryName;

   public String getModelName() {
      return modelName;
   }

   public ServiceConfigDto setModelName(String modelName) {
      this.modelName = modelName;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ServiceConfigDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getBasePacakgeName() {
      return basePacakgeName;
   }

   public ServiceConfigDto setBasePacakgeName(String basePacakgeName) {
      this.basePacakgeName = basePacakgeName;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public ServiceConfigDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }
}
