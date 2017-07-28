package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

import java.util.List;

public class TemplateDao {

   private String className;
   private String baseClassName;
   private String packageName;
   private String baseClassPackageName;
   private List<MethodDao> methods;

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

   public void setMethods(List<MethodDao> methods) {
      this.methods = methods;
   }
}
