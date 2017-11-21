package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.PubSubMethodDto;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseServiceDto {

   private String projectDirectoryName;
   private ClassDto<? extends MethodDto> interfacez;
   private ClassDto<? extends PubSubMethodDto> abstractClass;
   private Set<String> projectDependencies;
   private Set<String> exportedPackages;
   private EnumDto<?> topics;
   private IModel model;
   private List<ReceiveDto> receiveMethods;
   private List<PublishDto> publishMethods;
   private List<BasicPubSubDto> basicPubSubMethods;
   private List<BasicPubSubDto> basicSinkMethods;
   private List<CorrelationDto> correlationMethods;
   private List<TriggerDto> triggerRegistrationMethods;

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

   public ClassDto<? extends MethodDto> getInterface() {
      return interfacez;
   }

   public BaseServiceDto setInterface(ClassDto<? extends MethodDto> interfacez) {
      this.interfacez = interfacez;
      return this;
   }

   public ClassDto<? extends PubSubMethodDto> getAbstractClass() {
      return abstractClass;
   }

   public BaseServiceDto setAbstractClass(ClassDto<? extends PubSubMethodDto> abstractClass) {
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

   public EnumDto<?> getTopicsEnum() {
      return topics;
   }

   public BaseServiceDto setTopicsEnum(EnumDto<?> topics) {
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

}
