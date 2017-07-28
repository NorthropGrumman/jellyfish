package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

import java.util.List;

public class MethodDao {

   private String methodName;
   private boolean override;
   private boolean returns;
   private ArgumentDao returnArgument;
   private List<ArgumentDao> arguments;

   public String getMethodName() {
      return methodName;
   }

   public MethodDao setMethodName(String methodName) {
      this.methodName = methodName;
      return this;
   }

   public boolean isOverride() {
      return override;
   }

   public MethodDao setOverride(boolean override) {
      this.override = override;
      return this;
   }

   public boolean isReturns() {
      return returns;
   }

   public MethodDao setReturns(boolean returns) {
      this.returns = returns;
      return this;
   }

   public ArgumentDao getReturnArgument() {
      return returnArgument;
   }

   public MethodDao setReturnArgument(ArgumentDao returnArgument) {
      this.returnArgument = returnArgument;
      return this;
   }

   public List<ArgumentDao> getArguments() {
      return arguments;
   }

   public MethodDao setArguments(List<ArgumentDao> arguments) {
      this.arguments = arguments;
      return this;
   }
}
