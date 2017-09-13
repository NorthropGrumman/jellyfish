package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.List;
import java.util.Set;

/**
 * Represents a description of a Java class.  May also be used to represent an interface.
 */
public class ClassDto {

   private Set<String> imports;
   private String name;
   private String packageName;
   private List<MethodDto> methods;
   private boolean interfaze;
   private ClassDto baseClass;
   private ClassDto implementedInterface;

   /**
    * Gets the list of imports needed by this class.
    */
   public Set<String> getImports() {
      return imports;
   }

   public ClassDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   /**
    * Gets the unqualified name of this class.
    */
   public String getName() {
      return name;
   }

   public ClassDto setName(String name) {
      this.name = name;
      return this;
   }

   /**
    * Gets the name of the package of this class.
    */
   public String getPackageName() {
      return packageName;
   }

   public ClassDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   /**
    * Gets the methods defined by this class.
    */
   public List<MethodDto> getMethods() {
      return methods;
   }

   public ClassDto setMethods(List<MethodDto> methods) {
      this.methods = methods;
      return this;
   }

   /**
    * If true, this DTO represents an interface.
    */
   public boolean isInterface() {
      return interfaze;
   }

   public ClassDto setInterface(boolean interfaze) {
      this.interfaze = interfaze;
      return this;
   }

   /**
    * Gets the base class of this class or {@code null} if there is no base class.
    */
   public ClassDto getBaseClass() {
      return baseClass;
   }

   public ClassDto setBaseClass(ClassDto baseClass) {
      this.baseClass = baseClass;
      return this;
   }

   /**
    * Gets the interface this class implements (if any).
    */
   public ClassDto getImplementedInterface() {
      return implementedInterface;
   }

   public ClassDto setImplementedInterface(ClassDto implementedInterface) {
      this.implementedInterface = implementedInterface;
      return this;
   }
}
