package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

import java.util.List;
import java.util.Set;

public class TemplateDao {

   private String className;
   private String baseClassName;
   private String packageName;
   private String baseClassPackageName;
   private String interfaceName;
   private String interfacePackageName;
   private List<MethodDao> methods;
   private Set<String> imports;

   public String getClassName() {
      return className;
   }

   public TemplateDao setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getBaseClassName() {
      return baseClassName;
   }

   public TemplateDao setBaseClassName(String baseClassName) {
      this.baseClassName = baseClassName;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public TemplateDao setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getBaseClassPackageName() {
      return baseClassPackageName;
   }

   public TemplateDao setBaseClassPackageName(String baseClassPackageName) {
      this.baseClassPackageName = baseClassPackageName;
      return this;
   }

   public List<MethodDao> getMethods() {
      return methods;
   }

   public TemplateDao setMethods(List<MethodDao> methods) {
      this.methods = methods;
      return this;
   }

   public Set<String> getImports() {
      return imports;
   }

   public TemplateDao setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getInterfaceName() {
      return interfaceName;
   }

   public TemplateDao setInterfaceName(String interfaceName) {
      this.interfaceName = interfaceName;
      return this;
   }

   public String getInterfacePackageName() {
      return interfacePackageName;
   }

   public TemplateDao setInterfacePackageName(String interfacePackageName) {
      this.interfacePackageName = interfacePackageName;
      return this;
   }
}
