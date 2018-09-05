/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedJavaProtoField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedProtoField;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import org.apache.commons.lang3.StringUtils;

/**
 * A basic implementation of {@link IDataFieldGenerationService} for tests
 */
public class MockedDataFieldGenerationService implements IDataFieldGenerationService {

   private final IPackageNamingService packageService;

   public MockedDataFieldGenerationService() {
      this(new MockedPackageNamingService());
   }

   public MockedDataFieldGenerationService(IPackageNamingService packageService) {
      this.packageService = packageService;
   }

   @Override
   public IGeneratedJavaField getEventsField(IJellyFishCommandOptions options, IDataField field) {
      return new GeneratedField(field, false);
   }

   @Override
   public IGeneratedProtoField getMessagesField(IJellyFishCommandOptions options, IDataField field) {
      return new GeneratedField(field, true);
   }

   private class GeneratedField implements IGeneratedJavaField, IGeneratedProtoField, IGeneratedJavaProtoField {

      private final IDataField field;
      private final boolean proto;
      private final String pkg;
      private final String capitalizedName;

      public GeneratedField(IDataField field, boolean proto) {
         this.field = field;
         this.proto = proto;
         if (field.getType() == DataTypes.DATA || field.getType() == DataTypes.ENUM) {
            INamedChild<IPackage>
                  type =
                  field.getType() == DataTypes.DATA ? field.getReferencedDataType() : field.getReferencedEnumeration();
            if (proto) {
               pkg = packageService.getMessagePackageName(null, type);
            } else {
               pkg = packageService.getEventPackageName(null, type);
            }
         } else {
            pkg = null;
         }
         capitalizedName = StringUtils.capitalize(field.getName());
      }

      @Override
      public IDataField getDataField() {
         return field;
      }

      @Override
      public boolean isMultiple() {
         return field.getCardinality() == FieldCardinality.MANY;
      }

      @Override
      public String getJavaType() {
         switch (field.getType()) {
            case BOOLEAN:
               return isMultiple() ? "Boolean" : "boolean";
            case FLOAT:
               return isMultiple() ? "Float" : "float";
            case INT:
               return isMultiple() ? "Integer" : "int";
            case STRING:
               return "String";
            case DATA:
               return pkg + "." + field.getName();
            case ENUM:
               return pkg + "." + field.getName();
            default:
               throw new RuntimeException();
         }
      }

      @Override
      public String getJavaFieldName() {
         return field.getName();
      }

      @Override
      public String getJavaGetterName() {
         return proto && isMultiple() ? ("get" + capitalizedName + "List") : ("get" + capitalizedName);
      }

      @Override
      public String getJavaSetterName() {
         return proto && isMultiple() ? ("addAll" + capitalizedName) : ("set" + capitalizedName);
      }

      @Override
      public String getProtoFieldName() {
         return field.getName();
      }

      @Override
      public String getProtoType() {
         final String type;
         switch (field.getType()) {
            case BOOLEAN:
               type = "boolean";
               break;
            case FLOAT:
               type = "float";
               break;
            case INT:
               type = "sint32";
               break;
            case STRING:
               type = "string";
               break;
            case DATA:
               type = packageService.getMessagePackageName(null, field.getReferencedDataType()) + "." + field.getName();
               break;
            case ENUM:
               type =
                     packageService.getMessagePackageName(null, field.getReferencedEnumeration()) + "." + field
                           .getName();
               break;
            default:
               throw new RuntimeException();
         }
         return isMultiple() ? ("repeated " + type) : type;
      }

      @Override
      public IGeneratedJavaProtoField getJavaField() {
         return this;
      }

      @Override
      public String getRepeatedJavaCountName() {
         return "get" + capitalizedName + "Count";
      }

      @Override
      public String getRepeatedJavaAddName() {
         return "add" + capitalizedName;
      }

      @Override
      public String getRepeatedJavaGetterName() {
         return "get" + capitalizedName;
      }

   }

}
