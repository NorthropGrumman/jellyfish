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
package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto;

import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedProtoField;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessagesDataDto {

   private INamedChild<IPackage> data;
   private String packageName;
   private String className;
   private Function<IDataField, IGeneratedProtoField> dataService;

   public INamedChild<IPackage> getData() {
      return data;
   }

   public MessagesDataDto setData(INamedChild<IPackage> data) {
      this.data = data;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public MessagesDataDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getClassName() {
      return className;
   }

   public MessagesDataDto setClassName(String className) {
      this.className = className;
      return this;
   }

   /**
    *
    * @return Set of Imports
    */
   public Set<String> getImports() {
      return getFields().stream()
            .filter(field -> field.getType() == DataTypes.DATA || field.getType() == DataTypes.ENUM)
            .map(dataService::apply)
            .map(IGeneratedProtoField::getProtoType)
            .map(type -> type.replace('.', '/') + ".proto")
            .collect(Collectors.toCollection(TreeSet::new));
   }

   /**
    *
    * @return Collection of IDataField
    */
   public Collection<IDataField> getFields() {
      List<IDataField> fields = new ArrayList<>();

      IData parent = (IData) data;
      while (parent != null) {
         List<IDataField> dataFields = new ArrayList<>(parent.getFields());
         Collections.reverse(dataFields);
         fields.addAll(dataFields);
         parent = parent.getExtendedDataType().orElse(null);
      }

      Collections.reverse(fields);
      return fields;
   }

   public Function<IDataField, IGeneratedProtoField> getDataService() {
      return dataService;
   }

   public MessagesDataDto setDataService(Function<IDataField, IGeneratedProtoField> dataService) {
      this.dataService = dataService;
      return this;
   }

   public boolean isEnum() {
      return data instanceof IEnumeration;
   }

   /**
    *
    * @param field you want the type of
    * @return the field type
    */
   public String fieldType(IDataField field) {
      IGeneratedProtoField protoField = dataService.apply(field);
      if (protoField.isMultiple()) {
         return "repeated " + protoField.getProtoType();
      } else {
         return protoField.getProtoType();
      }
   }

   public String fieldName(IDataField field) {
      IGeneratedProtoField javaField = dataService.apply(field);
      return javaField.getProtoFieldName();
   }

}
