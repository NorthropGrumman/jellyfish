package com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto;

import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import java.util.function.Function;

public class EventsDataDto {

   private INamedChild<IPackage> data;
   private String packageName;
   private String className;
   private String extendedClass;
   private boolean _abstact;
   private Function<IDataField, IGeneratedJavaField> dataService;
   
   public INamedChild<IPackage> getData() {
      return data;
   }

   public EventsDataDto setData(INamedChild<IPackage> data) {
      this.data = data;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public EventsDataDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getClassName() {
      return className;
   }

   public EventsDataDto setClassName(String className) {
      this.className = className;
      return this;
   }
   
   public String getExtendedClass() {
      return extendedClass;
   }

   public EventsDataDto setExtendedClass(String extendedClass) {
      this.extendedClass = extendedClass;
      return this;
   }

   public boolean isAbstract() {
      return _abstact;
   }

   public EventsDataDto setAbstract(boolean _abstact) {
      this._abstact = _abstact;
      return this;
   }
   
   public Function<IDataField, IGeneratedJavaField> getDataService() {
      return dataService;
   }

   public EventsDataDto setDataService(Function<IDataField, IGeneratedJavaField> dataService) {
      this.dataService = dataService;
      return this;
   }

   public boolean isEnum() {
      return data instanceof IEnumeration;
   }
   
   public String fieldType(IDataField field) {
      IGeneratedJavaField javaField = dataService.apply(field);
      if (javaField.isMultiple()) {
         return "java.util.List<" + javaField.getJavaType() + ">";
      } else {
         return javaField.getJavaType();
      }
   }
   
   public String fieldName(IDataField field) {
      IGeneratedJavaField javaField = dataService.apply(field);
      return javaField.getJavaFieldName();
   }
   
   public String fieldGetter(IDataField field) {
      IGeneratedJavaField javaField = dataService.apply(field);
      return javaField.getJavaGetterName();
   }
   
   public String fieldSetter(IDataField field) {
      IGeneratedJavaField javaField = dataService.apply(field);
      return javaField.getJavaSetterName();
   }
   
}
