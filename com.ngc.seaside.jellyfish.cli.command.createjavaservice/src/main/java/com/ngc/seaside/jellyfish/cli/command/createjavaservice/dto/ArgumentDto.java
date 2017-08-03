package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

public class ArgumentDto {

   private String argumentName;
   private String argumentClassName;
   private String argumentPackageName;

   public String getArgumentName() {
      return argumentName;
   }

   public ArgumentDto setArgumentName(String argumentName) {
      this.argumentName = argumentName;
      return this;
   }

   public String getArgumentClassName() {
      return argumentClassName;
   }

   public ArgumentDto setArgumentClassName(String argumentClassName) {
      this.argumentClassName = argumentClassName;
      return this;
   }

   public String getArgumentPackageName() {
      return argumentPackageName;
   }

   public ArgumentDto setArgumentPackageName(String argumentPackageName) {
      this.argumentPackageName = argumentPackageName;
      return this;
   }
}
