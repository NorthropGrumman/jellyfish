package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector.dto;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ConnectorDto {
   private IModel model;
   private String packageName;
   private Function<INamedChild<IPackage>, String> eventsPackageName;
   private Function<INamedChild<IPackage>, String> messagesPackageName;
   private String transportTopicsClass;
   private String projectName;
   private Set<String> projectDependencies;
   private Map<String, IData> inputTopics;
   private Map<String, IData> outputTopics;
   private Set<INamedChild<IPackage>> allInputs;
   private Set<INamedChild<IPackage>> allOutputs;
   private Map<String, Set<String>> topicRequirements;

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
   
   public Function<INamedChild<IPackage>, String> getEventsPackageName() {
      return eventsPackageName;
   }

   public void setEventsPackageName(Function<INamedChild<IPackage>, String> eventsPackageName) {
      this.eventsPackageName = eventsPackageName;
   }
   
   public Function<INamedChild<IPackage>, String> getMessagesPackageName() {
      return messagesPackageName;
   }

   public void setMessagesPackageName(Function<INamedChild<IPackage>, String> messagesPackageName) {
      this.messagesPackageName = messagesPackageName;
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

   public Map<String, Set<String>> getTopicRequirements() {
      return topicRequirements;
   }

   public ConnectorDto setTopicRequirements(Map<String, Set<String>> topicRequirements) {
      this.topicRequirements = topicRequirements;
      return this;
   }
}
