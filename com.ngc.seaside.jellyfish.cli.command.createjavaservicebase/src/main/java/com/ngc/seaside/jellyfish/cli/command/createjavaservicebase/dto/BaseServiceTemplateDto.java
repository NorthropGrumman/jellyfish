package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.MethodDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.TemplateDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseServiceTemplateDto extends TemplateDto {

   private Set<String> exportedPackages;
   private Set<String> transportTopics;
   private List<MethodDto> publishingMethods;
   private List<MethodDto> receivingMethods;

   public String getExportedPackagesSnippet() {
      return exportedPackages.stream()
            .collect(Collectors.joining(", "));
   }

   public Set<String> getExportedPackages() {
      return exportedPackages;
   }

   public BaseServiceTemplateDto setExportedPackages(Set<String> exportedPackages) {
      this.exportedPackages = exportedPackages;
      return this;
   }

   public Set<String> getTransportTopics() {
      return transportTopics;
   }

   public BaseServiceTemplateDto setTransportTopics(Set<String> transportTopics) {
      this.transportTopics = transportTopics;
      return this;
   }

   public List<MethodDto> getPublishingMethods() {
      return publishingMethods;
   }

   public BaseServiceTemplateDto setPublishingMethods(
         List<MethodDto> publishingMethods) {
      this.publishingMethods = publishingMethods;
      return this;
   }

   public List<MethodDto> getReceivingMethods() {
      return receivingMethods;
   }

   public BaseServiceTemplateDto setReceivingMethods(
         List<MethodDto> receivingMethods) {
      this.receivingMethods = receivingMethods;
      return this;
   }
}
