/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
   private boolean abstractBool;
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
      return abstractBool;
   }

   public EventsDataDto setAbstract(boolean abstractBool) {
      this.abstractBool = abstractBool;
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

   /**
    *
    * @param field that you want a java type for
    * @return String of the field type
    */
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
