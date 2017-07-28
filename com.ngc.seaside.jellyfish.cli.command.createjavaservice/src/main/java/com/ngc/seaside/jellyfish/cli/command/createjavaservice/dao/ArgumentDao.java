package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

public class ArgumentDao {

   private String argumentName;
   private String argumentClassName;
   private String argumentPackageName;

   public String getArgumentName() {
      return argumentName;
   }

   public ArgumentDao setArgumentName(String argumentName) {
      this.argumentName = argumentName;
      return this;
   }

   public String getArgumentClassName() {
      return argumentClassName;
   }

   public ArgumentDao setArgumentClassName(String argumentClassName) {
      this.argumentClassName = argumentClassName;
      return this;
   }

   public String getArgumentPackageName() {
      return argumentPackageName;
   }

   public ArgumentDao setArgumentPackageName(String argumentPackageName) {
      this.argumentPackageName = argumentPackageName;
      return this;
   }
}
