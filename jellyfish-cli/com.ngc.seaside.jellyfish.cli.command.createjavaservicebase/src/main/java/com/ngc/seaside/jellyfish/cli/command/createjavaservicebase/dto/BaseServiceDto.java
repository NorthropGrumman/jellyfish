package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseServiceDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectDirectoryName;
   private ClassDto interfacez;
   private ClassDto abstractClass;
   private Set<String> projectDependencies;
   private Set<String> exportedPackages;
   private EnumDto topics;
   private IModel model;
   private boolean correlationRequestHandlingEnabled;
   private boolean correlationServiceRequired;
   private List<ReceiveDto> receiveMethods = new ArrayList<>();
   private List<PublishDto> publishMethods = new ArrayList<>();
   private List<BasicPubSubDto> basicPubSubMethods = new ArrayList<>(); 
   private List<BasicPubSubDto> basicSinkMethods = new ArrayList<>();
   private List<CorrelationDto> correlationMethods = new ArrayList<>();
   private List<TriggerDto> triggerRegistrationMethods = new ArrayList<>();
   private List<ComplexScenarioDto> complexScenarios = new ArrayList<>();

   public BaseServiceDto(IBuildManagementService buildManagementService,
                         IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getExportedPackagesSnippet() {
      return exportedPackages.stream()
            .collect(Collectors.joining(", "));
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public BaseServiceDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public ClassDto getInterface() {
      return interfacez;
   }

   public BaseServiceDto setInterface(ClassDto interfacez) {
      this.interfacez = interfacez;
      return this;
   }

   public ClassDto getAbstractClass() {
      return abstractClass;
   }

   public BaseServiceDto setAbstractClass(ClassDto abstractClass) {
      this.abstractClass = abstractClass;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public BaseServiceDto setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
      return this;
   }

   public Set<String> getExportedPackages() {
      return exportedPackages;
   }

   public BaseServiceDto setExportedPackages(Set<String> exportedPackages) {
      this.exportedPackages = exportedPackages;
      return this;
   }

   public EnumDto getTopicsEnum() {
      return topics;
   }

   public BaseServiceDto setTopicsEnum(EnumDto topics) {
      this.topics = topics;
      return this;
   }

   public IModel getModel() {
      return model;
   }

   public BaseServiceDto setModel(IModel model) {
      this.model = model;
      return this;
   }

   public List<ReceiveDto> getReceiveMethods() {
      return receiveMethods;
   }

   public BaseServiceDto setReceiveMethods(List<ReceiveDto> receiveMethods) {
      this.receiveMethods = receiveMethods;
      return this;
   }

   public List<PublishDto> getPublishMethods() {
      return publishMethods;
   }

   public BaseServiceDto setPublishMethods(List<PublishDto> publishMethods) {
      this.publishMethods = publishMethods;
      return this;
   }

   public List<BasicPubSubDto> getBasicPubSubMethods() {
      return basicPubSubMethods;
   }

   public BaseServiceDto setBasicPubSubMethods(List<BasicPubSubDto> basicPubSubMethods) {
      this.basicPubSubMethods = basicPubSubMethods;
      return this;
   }

   public List<BasicPubSubDto> getBasicSinkMethods() {
      return basicSinkMethods;
   }

   public BaseServiceDto setBasicSinkMethods(List<BasicPubSubDto> basicSinkMethods) {
      this.basicSinkMethods = basicSinkMethods;
      return this;
   }

   public List<CorrelationDto> getCorrelationMethods() {
      return correlationMethods;
   }

   public BaseServiceDto setCorrelationMethods(List<CorrelationDto> correlationMethods) {
      this.correlationMethods = correlationMethods;
      return this;
   }

   public List<TriggerDto> getTriggerRegistrationMethods() {
      return triggerRegistrationMethods;
   }

   public BaseServiceDto setTriggerRegistrationMethods(List<TriggerDto> triggerRegistrationMethods) {
      this.triggerRegistrationMethods = triggerRegistrationMethods;
      return this;
   }

   public List<ComplexScenarioDto> getComplexScenarios() {
      return complexScenarios;
   }

   public BaseServiceDto setComplexScenarios(List<ComplexScenarioDto> complexScenarios) {
      this.complexScenarios = complexScenarios;
      return this;
   }

   public boolean isCorrelationRequestHandlingEnabled() {
      return correlationRequestHandlingEnabled;
   }

   public BaseServiceDto setCorrelationRequestHandlingEnabled(boolean correlationRequestHandlingEnabled) {
      this.correlationRequestHandlingEnabled = correlationRequestHandlingEnabled;
      return this;
   }

   public boolean isCorrelationServiceRequired() {
      return correlationServiceRequired;
   }

   public BaseServiceDto setCorrelationServiceRequired(boolean correlationServiceRequired) {
      this.correlationServiceRequired = correlationServiceRequired;
      return this;
   }

   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
