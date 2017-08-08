package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import java.util.Set;

public class AbstractServiceDto {

   private Set<String> imports;
   private String className;
   private String packageName;
   private String modelName;

   public Set<String> getImports() {
      return imports;
   }

   public AbstractServiceDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getClassName() {
      return className;
   }

   public AbstractServiceDto setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public AbstractServiceDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getModelName() {
      return modelName;
   }

   public AbstractServiceDto setModelName(String modelName) {
      this.modelName = modelName;
      return this;
   }
}
