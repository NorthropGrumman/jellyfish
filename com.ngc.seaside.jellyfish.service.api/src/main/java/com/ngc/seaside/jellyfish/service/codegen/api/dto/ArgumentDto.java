package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an argument to a Java method.
 */
public class ArgumentDto implements TypeDto<ArgumentDto> {

   private String name;
   private String typeName;
   private String packageName;
   private List<TypeDto<?>> types = new ArrayList<>();

   /**
    * Gets a string that contains the verbatim Java code for the type parameters of this argument.
    */
   public String getTypeSnippet() {
      if (types == null || types.isEmpty()) {
         return "";
      }
      return "<" + types.stream().map(TypeDto::getTypeName).collect(Collectors.joining(", ")) + ">";
   }

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
   public String getTypeName() {
      return typeName;
   }

   public ArgumentDto setTypeName(String name) {
      this.typeName = name;
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

   public List<TypeDto<?>> getTypes() {
      return types;
   }

   public ArgumentDto setTypes(List<TypeDto<?>> types) {
      this.types = types;
      return this;
   }
}
