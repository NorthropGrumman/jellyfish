package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a description of a Java method.
 */
public class MethodDto {

   private String name;
   private boolean override;
   private boolean returns;
   private ArgumentDto returnArgument;
   private List<ArgumentDto> arguments;

   /**
    * Gets the method's signature. Two methods can be considered equal if their signatures are the same.
    */
   public String getMethodSignature() {
      return (returns ? returnArgument.getFullyQualifiedName() : "void") + " " + name + "("
         + arguments.stream().map(ArgumentDto::getTypeName).collect(Collectors.joining(",")) + ")";
   }

   /**
    * Gets a string that contains the verbatim Java code for the return type of this method.
    */
   public String getReturnSnippet() {
      return returns ? returnArgument.getTypeName() : "void";
   }

   /**
    * Gets a string that contains the verbatim Java code for the argument list of this method.
    */
   public String getArgumentsListSnippet() {
      return arguments.stream()
                      .map(a -> a.getTypeName() + a.getTypeSnippet() + " " + a.getName())
                      .collect(Collectors.joining(", "));
   }

   /**
    * Gets the name of this method.
    */
   public String getName() {
      return name;
   }

   public MethodDto setName(String name) {
      this.name = name;
      return this;
   }

   /**
    * If true, this method should have an {@code Override} annotation.
    */
   public boolean isOverride() {
      return override;
   }

   public MethodDto setOverride(boolean override) {
      this.override = override;
      return this;
   }

   /**
    * If true, this method returns a value.
    */
   public boolean isReturns() {
      return returns;
   }

   public MethodDto setReturns(boolean returns) {
      this.returns = returns;
      return this;
   }

   /**
    * Gets the return argument of this method or {@code null} if this method does not return anything.
    */
   public ArgumentDto getReturnArgument() {
      return returnArgument;
   }

   public MethodDto setReturnArgument(ArgumentDto returnArgument) {
      this.returns = returnArgument != null;
      this.returnArgument = returnArgument;
      return this;
   }

   /**
    * Gets the arguments to this method.
    */
   public List<ArgumentDto> getArguments() {
      return arguments;
   }

   public MethodDto setArguments(List<ArgumentDto> arguments) {
      this.arguments = arguments;
      return this;
   }
}
