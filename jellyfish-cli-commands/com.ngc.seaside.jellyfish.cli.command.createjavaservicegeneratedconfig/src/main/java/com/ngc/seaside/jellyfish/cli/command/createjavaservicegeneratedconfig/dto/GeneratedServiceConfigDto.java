package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GeneratedServiceConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String modelName;
   private String packageName;
   private String projectDirectoryName;
   private String baseProjectArtifactName;
   private String connectorClassname;
   private List<TransportProviderDto> transportProviders = new ArrayList<>();
   private Set<String> transportProviderDependencies = new LinkedHashSet<>();
   private List<String> subscribers = new ArrayList<>();

   public GeneratedServiceConfigDto(IBuildManagementService buildManagementService,
                                    IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getModelName() {
      return modelName;
   }

   public GeneratedServiceConfigDto setModelName(String modelName) {
      this.modelName = modelName;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public GeneratedServiceConfigDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public String getBaseProjectArtifactName() {
      return baseProjectArtifactName;
   }

   public GeneratedServiceConfigDto setBaseProjectArtifactName(String baseProjectArtifactName) {
      this.baseProjectArtifactName = baseProjectArtifactName;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public GeneratedServiceConfigDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public List<TransportProviderDto> getTransportProviders() {
      return this.transportProviders;
   }

   public GeneratedServiceConfigDto addTransportProvider(TransportProviderDto dto) {
      this.transportProviders.add(dto);
      return this;
   }

   public Set<String> getTransportProviderDependencies() {
      return transportProviderDependencies;
   }

   public GeneratedServiceConfigDto addTransportProviderDependencies(Collection<String> dependencies) {
      this.transportProviderDependencies.addAll(dependencies);
      return this;
   }

   public String getConnectorClassname() {
      return this.connectorClassname;
   }

   public GeneratedServiceConfigDto setConnectorClassname(String connectorClassname) {
      this.connectorClassname = connectorClassname;
      return this;
   }

   public List<String> getSubscribers() {
      return subscribers;
   }

   public GeneratedServiceConfigDto addSubscriberClassname(String subscriberClassname) {
      this.subscribers.add(subscriberClassname);
      return this;
   }

   /**
    * @param groupAndArtifactId String of the group ID that you want formatted
    * @return String of formatted dependency
    */
   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
