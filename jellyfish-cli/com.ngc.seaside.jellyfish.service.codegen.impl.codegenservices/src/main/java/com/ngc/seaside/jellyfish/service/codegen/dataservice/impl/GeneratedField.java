/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.codegen.dataservice.impl;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedJavaProtoField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedProtoField;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratedField implements IGeneratedJavaField, IGeneratedProtoField {

   private static final Pattern UNDERSCORE_PATTERN = Pattern.compile("(?<=_|\\d)[a-zA-Z]");
   private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
         "abstract",
         "assert",
         "boolean",
         "break",
         "byte",
         "case",
         "catch",
         "char",
         "class",
         "const",
         "continue",
         "default",
         "do",
         "double",
         "else",
         "extends",
         "false",
         "final",
         "finally",
         "float",
         "for",
         "goto",
         "if",
         "implements",
         "import",
         "instanceof",
         "int",
         "interface",
         "long",
         "native",
         "new",
         "null",
         "package",
         "private",
         "protected",
         "public",
         "return",
         "short",
         "static",
         "strictfp",
         "super",
         "switch",
         "synchronized",
         "this",
         "throw",
         "throws",
         "transient",
         "true",
         "try",
         "void",
         "volatile",
         "while"));

   private final IDataField field;
   private final String javaType;
   private final String javaFieldName;
   private final String javaGetterName;
   private final String javaSetterName;
   private final String protoType;
   private final String protoFieldName;
   private final String protoJavaType;
   private final String protoJavaGetterName;
   private final String protoJavaSetterName;
   private final String protoRepeatedJavaCountName;
   private final String protoRepeatedJavaAddName;
   private final String protoRepeatedJavaGetterName;

   private final IGeneratedJavaProtoField protoJavaField = new GeneratedJavaProtoField();

   private GeneratedField(IDataField field,
                          String javaType,
                          String javaFieldName,
                          String javaGetterName,
                          String javaSetterName,
                          String protoType,
                          String protoFieldName,
                          String protoJavaType,
                          String protoJavaGetterName,
                          String protoJavaSetterName,
                          String protoRepeatedJavaCountName,
                          String protoRepeatedJavaAddName,
                          String protoRepeatedJavaGetterName) {
      this.field = field;
      this.javaType = javaType;
      this.javaFieldName = javaFieldName;
      this.javaGetterName = javaGetterName;
      this.javaSetterName = javaSetterName;
      this.protoType = protoType;
      this.protoFieldName = protoFieldName;
      this.protoJavaType = protoJavaType;
      this.protoJavaGetterName = protoJavaGetterName;
      this.protoJavaSetterName = protoJavaSetterName;
      this.protoRepeatedJavaCountName = protoRepeatedJavaCountName;
      this.protoRepeatedJavaAddName = protoRepeatedJavaAddName;
      this.protoRepeatedJavaGetterName = protoRepeatedJavaGetterName;
   }

   /**
    *
    * @param field to generate
    * @param options for the field
    * @param packageNamingService for the field
    * @return
    */
   public static GeneratedField of(IDataField field,
                                   IJellyFishCommandOptions options,
                                   IPackageNamingService packageNamingService) {
      final String singleType;
      final String multipleType;
      final String protoType;
      final String protoSingleType;
      final String protoMultipleType;
      switch (field.getType()) {
         case DATA:
            IData dataReference = field.getReferencedDataType();
            singleType =
                  packageNamingService.getEventPackageName(options, dataReference) + "." + dataReference.getName();
            multipleType = singleType;
            protoType =
                  packageNamingService.getMessagePackageName(options, dataReference) + "." + dataReference.getName();
            protoSingleType = protoType;
            protoMultipleType = protoSingleType;
            break;
         case ENUM:
            IEnumeration enumReference = field.getReferencedEnumeration();
            singleType =
                  packageNamingService.getEventPackageName(options, enumReference) + "." + enumReference.getName();
            multipleType = singleType;
            protoType =
                  packageNamingService.getMessagePackageName(options, enumReference) + "." + enumReference.getName();
            protoSingleType = protoType;
            protoMultipleType = protoSingleType;
            break;
         case STRING:
            singleType = String.class.getCanonicalName();
            multipleType = singleType;
            protoType = "string";
            protoSingleType = singleType;
            protoMultipleType = protoSingleType;
            break;
         case BOOLEAN:
            singleType = "boolean";
            multipleType = Boolean.class.getCanonicalName();
            protoType = "bool";
            protoSingleType = singleType;
            protoMultipleType = multipleType;
            break;
         case FLOAT:
            singleType = "float";
            multipleType = Float.class.getCanonicalName();
            protoType = "float";
            protoSingleType = singleType;
            protoMultipleType = multipleType;
            break;
         case INT:
            singleType = "int";
            multipleType = Integer.class.getCanonicalName();
            protoType = "sint32";
            protoSingleType = singleType;
            protoMultipleType = multipleType;
            break;
         default:
            throw new IllegalStateException("Unknown type: " + field.getType());
      }

      final String originalFieldName = field.getName();
      StringBuffer buffer = new StringBuffer(originalFieldName.length());
      Matcher m = UNDERSCORE_PATTERN.matcher(originalFieldName);
      while (m.find()) {
         if (buffer.length() == 0
                 && (originalFieldName.startsWith("_") || Character.isDigit(originalFieldName.charAt(0)))) {
            m.appendReplacement(buffer, m.group(0));
         } else {
            m.appendReplacement(buffer, m.group(0).toUpperCase());
         }
      }
      m.appendTail(buffer);
      String fieldName = buffer.toString().replace("_", "");

      final String capFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
      final String javaFieldName;
      if (originalFieldName.startsWith("_") || RESERVED_WORDS.contains(originalFieldName)) {
         javaFieldName = "_" + originalFieldName;
      } else {
         javaFieldName = originalFieldName;
      }

      final String repeatedCountName = "get" + capFieldName + "Count";
      final String repeatedAddName = "add" + capFieldName;
      final String repeatedGetterName = "get" + capFieldName;

      if (field.getCardinality() == FieldCardinality.SINGLE) {
         return new GeneratedField(field, singleType, javaFieldName, "get" + capFieldName, "set" + capFieldName,
                                   protoType, fieldName, protoSingleType, "get" + capFieldName, "set" + capFieldName,
                                   repeatedCountName,
                                   repeatedAddName, repeatedGetterName);
      } else {
         return new GeneratedField(field, multipleType, javaFieldName, "get" + capFieldName, "set" + capFieldName,
                                   protoType, fieldName, protoMultipleType, "get" + capFieldName + "List",
                                   "addAll" + capFieldName,
                                   repeatedCountName, repeatedAddName, repeatedGetterName);
      }
   }

   @Override
   public IDataField getDataField() {
      return this.field;
   }

   @Override
   public boolean isMultiple() {
      return this.field.getCardinality() == FieldCardinality.MANY;
   }

   @Override
   public String getJavaType() {
      return this.javaType;
   }

   @Override
   public String getJavaFieldName() {
      return this.javaFieldName;
   }

   @Override
   public String getJavaGetterName() {
      return this.javaGetterName;
   }

   @Override
   public String getJavaSetterName() {
      return this.javaSetterName;
   }

   @Override
   public String getProtoFieldName() {
      return this.protoFieldName;
   }

   @Override
   public String getProtoType() {
      return this.protoType;
   }

   @Override
   public IGeneratedJavaProtoField getJavaField() {
      return this.protoJavaField;
   }

   private class GeneratedJavaProtoField implements IGeneratedJavaProtoField {

      @Override
      public IDataField getDataField() {
         return GeneratedField.this.getDataField();
      }

      @Override
      public boolean isMultiple() {
         return GeneratedField.this.isMultiple();
      }

      @Override
      public String getJavaType() {
         return GeneratedField.this.protoJavaType;
      }

      @Override
      public String getJavaFieldName() {
         throw new UnsupportedOperationException(
               "The java field name from the generated protocol buffer message is unsupported");
      }

      @Override
      public String getJavaGetterName() {
         return GeneratedField.this.protoJavaGetterName;
      }

      @Override
      public String getJavaSetterName() {
         return GeneratedField.this.protoJavaSetterName;
      }

      @Override
      public String getRepeatedJavaCountName() {
         if (!isMultiple()) {
            throw new IllegalStateException("This field is not a repeated field");
         }
         return GeneratedField.this.protoRepeatedJavaCountName;
      }

      @Override
      public String getRepeatedJavaAddName() {
         if (!isMultiple()) {
            throw new IllegalStateException("This field is not a repeated field");
         }
         return GeneratedField.this.protoRepeatedJavaAddName;
      }

      @Override
      public String getRepeatedJavaGetterName() {
         if (!isMultiple()) {
            throw new IllegalStateException("This field is not a repeated field");
         }
         return GeneratedField.this.protoRepeatedJavaGetterName;
      }

   }

}
