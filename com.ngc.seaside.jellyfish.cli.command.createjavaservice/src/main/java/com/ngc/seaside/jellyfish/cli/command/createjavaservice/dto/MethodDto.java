package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import java.util.List;
import java.util.stream.Collectors;

public class MethodDto {

   private String methodName;
   private boolean override;
   private boolean returns;
   private ArgumentDto returnArgument;
   private List<ArgumentDto> arguments;

   public String getReturnSnippet() {
      return returns ? returnArgument.getArgumentClassName() : "void";
   }

   public String getArgumentsListSnippet() {
      return arguments.stream()
            .map(a -> a.getArgumentClassName() + " " + a.getArgumentName())
            .collect(Collectors.joining(", "));
   }

   public String getMethodName() {
      return methodName;
   }

   public MethodDto setMethodName(String methodName) {
      this.methodName = methodName;
      return this;
   }

   public boolean isOverride() {
      return override;
   }

   public MethodDto setOverride(boolean override) {
      this.override = override;
      return this;
   }

   public boolean isReturns() {
      return returns;
   }

   public MethodDto setReturns(boolean returns) {
      this.returns = returns;
      return this;
   }

   public ArgumentDto getReturnArgument() {
      return returnArgument;
   }

   public MethodDto setReturnArgument(ArgumentDto returnArgument) {
      this.returnArgument = returnArgument;
      return this;
   }

   public List<ArgumentDto> getArguments() {
      return arguments;
   }

   public MethodDto setArguments(List<ArgumentDto> arguments) {
      this.arguments = arguments;
      return this;
   }
}
