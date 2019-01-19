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
package com.ngc.seaside.jellyfish.cli.command.createjavaprotobufconnector.dto;

import com.google.common.base.Objects;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public class ConnectorDto {

   private IModel model;
   private String packageName;
   private Function<IData, Collection<IDataField>> fields;
   private String transportTopicsClass;
   private String projectName;
   private Set<String> projectDependencies = new LinkedHashSet<>();
   private Map<String, IData> inputTopics = new TreeMap<>();
   private Map<String, IData> outputTopics = new TreeMap<>();
   private Map<String, ReqResTopic> requestTopics = new TreeMap<>();
   private Set<INamedChild<IPackage>> allInputs = new LinkedHashSet<>();
   private Set<INamedChild<IPackage>> allOutputs = new LinkedHashSet<>();
   private Map<String, Set<String>> topicRequirements = new TreeMap<>();
   private IPackageNamingService packageService;
   private IDataFieldGenerationService dataFieldService;
   private IJellyFishCommandOptions options;
   private boolean requiresInjectedService;
   private ClassDto serviceInterface;

   public IModel getModel() {
      return model;
   }

   public ConnectorDto setModel(IModel model) {
      this.model = model;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ConnectorDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public Function<IData, Collection<IDataField>> getFields() {
      return fields;
   }

   public void setFields(Function<IData, Collection<IDataField>> fields) {
      this.fields = fields;
   }

   public String getTransportTopicsClass() {
      return transportTopicsClass;
   }

   public void setTransportTopicsClass(String transportTopicsClass) {
      this.transportTopicsClass = transportTopicsClass;
   }

   public String getProjectName() {
      return projectName;
   }

   public ConnectorDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public void setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
   }

   public Set<INamedChild<IPackage>> getAllInputs() {
      return allInputs;
   }

   public ConnectorDto setAllInputs(Set<INamedChild<IPackage>> allInputs) {
      this.allInputs = allInputs;
      return this;
   }

   public Set<INamedChild<IPackage>> getAllOutputs() {
      return allOutputs;
   }

   public ConnectorDto setAllOutputs(Set<INamedChild<IPackage>> allOutputs) {
      this.allOutputs = allOutputs;
      return this;
   }

   public Map<String, IData> getInputTopics() {
      return inputTopics;
   }

   public ConnectorDto setInputTopics(Map<String, IData> inputTopics) {
      this.inputTopics = inputTopics;
      return this;
   }

   public Map<String, IData> getOutputTopics() {
      return outputTopics;
   }

   public ConnectorDto setOutputTopics(Map<String, IData> outputTopics) {
      this.outputTopics = outputTopics;
      return this;
   }

   public Map<String, ReqResTopic> getRequestTopics() {
      return requestTopics;
   }

   public ConnectorDto setRequestTopics(Map<String, ReqResTopic> requestTopics) {
      this.requestTopics = requestTopics;
      return this;
   }

   public Map<String, Set<String>> getTopicRequirements() {
      return topicRequirements;
   }

   public ConnectorDto setTopicRequirements(Map<String, Set<String>> topicRequirements) {
      this.topicRequirements = topicRequirements;
      return this;
   }

   public IPackageNamingService getPackageService() {
      return packageService;
   }

   public ConnectorDto setPackageService(IPackageNamingService packageService) {
      this.packageService = packageService;
      return this;
   }

   public IDataFieldGenerationService getDataFieldService() {
      return dataFieldService;
   }

   public ConnectorDto setDataFieldService(IDataFieldGenerationService dataFieldService) {
      this.dataFieldService = dataFieldService;
      return this;
   }

   public IJellyFishCommandOptions getOptions() {
      return options;
   }

   public ConnectorDto setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }

   public boolean isRequiresInjectedService() {
      return requiresInjectedService;
   }

   public ConnectorDto setRequiresInjectedService(boolean requiresInjectedService) {
      this.requiresInjectedService = requiresInjectedService;
      return this;
   }

   public ClassDto getServiceInterface() {
      return serviceInterface;
   }

   public ConnectorDto setServiceInterface(ClassDto serviceInterface) {
      this.serviceInterface = serviceInterface;
      return this;
   }

   public String eventPackage(INamedChild<IPackage> child) {
      return packageService.getEventPackageName(options, child);
   }

   public String messagePackage(INamedChild<IPackage> child) {
      return packageService.getMessagePackageName(options, child);
   }

   public boolean isConverted(INamedChild<IPackage> field) {
      return true;
   }

   public boolean isMultiple(IDataField field) {
      return field.getCardinality() == FieldCardinality.MANY;
   }

   /**
    * Returns a method to convert from type1 to type2.
    *
    * @param child     INamedChild on which the conversion is based
    * @param javaType1 type to convert from
    * @param javaType2 type to convert to
    * @param argument  argument to convert
    * @return a method to convert from type1 to type2
    */
   private static String converterName(INamedChild<?> child, String javaType1, String javaType2, String argument) {
      if (Objects.equal(javaType1, javaType2)) {
         return argument;
      }
      if (child instanceof IDataField) {
         throw new UnsupportedOperationException("Primitive conversion is currently unsupported");
      } else if (child instanceof IData || child instanceof IEnumeration) {
         return "convert(" + argument + ")";
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public String eventToMessageConvert(INamedChild<?> field, String argument) {
      if (field instanceof IData || field instanceof IEnumeration) {
         return converterName(field,
                              eventPackage((INamedChild<IPackage>) field) + '.' + field.getName(),
                              messagePackage((INamedChild<IPackage>) field) + '.' + field.getName(),
                              argument);
      } else if (field instanceof IDataField) {
         switch (((IDataField) field).getType()) {
            case DATA:
               return eventToMessageConvert(((IDataField) field).getReferencedDataType(), argument);
            case ENUM:
               return eventToMessageConvert(((IDataField) field).getReferencedEnumeration(), argument);
            default:
               break;
         }
         String javaType = dataFieldService.getEventsField(options, (IDataField) field).getJavaType();
         String javaProtoType = dataFieldService.getMessagesField(options, (IDataField) field)
               .getJavaField()
               .getJavaType();
         return converterName(field, javaType, javaProtoType, argument);
      }
      throw new IllegalStateException("Unknown parameter type: " + field);
   }

   @SuppressWarnings("unchecked")
   public String messageToEventConvert(INamedChild<?> field, String argument) {
      if (field instanceof IData || field instanceof IEnumeration) {
         return converterName(field,
                              messagePackage((INamedChild<IPackage>) field) + '.' + field.getName(),
                              eventPackage((INamedChild<IPackage>) field) + '.' + field.getName(),
                              argument);
      } else if (field instanceof IDataField) {
         switch (((IDataField) field).getType()) {
            case DATA:
               return messageToEventConvert(((IDataField) field).getReferencedDataType(), argument);
            case ENUM:
               return messageToEventConvert(((IDataField) field).getReferencedEnumeration(), argument);
            default:
               break;
         }
         String javaType = dataFieldService.getEventsField(options, (IDataField) field).getJavaType();
         String javaProtoType = dataFieldService.getMessagesField(options, (IDataField) field)
               .getJavaField()
               .getJavaType();
         return converterName(field, javaProtoType, javaType, argument);
      }
      throw new IllegalStateException("Unknown parameter type: " + field);
   }

   public String eventType(IDataField field) {
      return dataFieldService.getEventsField(options, field).getJavaType();
   }

   public String messageType(IDataField field) {
      return dataFieldService.getMessagesField(options, field).getJavaField().getJavaType();
   }

   public String eventGetter(IDataField field) {
      return dataFieldService.getEventsField(options, field).getJavaGetterName();
   }

   public String eventSetter(IDataField field) {
      return dataFieldService.getEventsField(options, field).getJavaSetterName();
   }

   public String messageGetter(IDataField field) {
      return dataFieldService.getMessagesField(options, field).getJavaField().getJavaGetterName();
   }

   public String messageSetter(IDataField field) {
      return dataFieldService.getMessagesField(options, field).getJavaField().getJavaSetterName();
   }

   public String messageRepeatedAdder(IDataField field) {
      return dataFieldService.getMessagesField(options, field).getJavaField().getRepeatedJavaAddName();
   }

   public String messageRepeatedCount(IDataField field) {
      return dataFieldService.getMessagesField(options, field).getJavaField().getRepeatedJavaCountName();
   }

   public String getServiceFieldName() {
      return StringUtils.uncapitalize(model.getName());
   }
}
