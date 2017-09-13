package com.ngc.seaside.jellyfish.service.codegen.api.dto;

/**
 * Represents an argument to a Java method.
 */
public class ArgumentDto {

   private String name;
   private String className;
   private String packageName;

   /**
    * Gets the name of this argument.
    */
   public String getName() {
      return name;
   }

   public ArgumentDto setName(String name) {
      this.name = name;
      return this;
   }

   /**
    * Gets the unqualified class name of this argument's type.
    */
   public String getClassName() {
      return className;
   }

   public ArgumentDto setClassName(String className) {
      this.className = className;
      return this;
   }

   /**
    * Gets the package name of this argument's type.
    */
   public String getPackageName() {
      return packageName;
   }

   public ArgumentDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }
}
