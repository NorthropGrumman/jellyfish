package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeneratedServiceConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectDirectoryName;
   private String baseProjectName;

   private Set<String> compileDependencies = new LinkedHashSet<>();
   private Set<String> defaultBundles = new LinkedHashSet<>();

   /**
    * Constructor.
    * 
    * @param buildManagementService build management service
    * @param options jellyfish command options
    */
   public GeneratedServiceConfigDto(IBuildManagementService buildManagementService,
                                    IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getBaseProjectName() {
      return baseProjectName;
   }

   public GeneratedServiceConfigDto setBaseProjectName(String baseProjectName) {
      this.baseProjectName = baseProjectName;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public GeneratedServiceConfigDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public Set<String> getCompileDependencies() {
      return compileDependencies;
   }

   public GeneratedServiceConfigDto addCompileDependencies(Collection<String> dependencies) {
      this.compileDependencies.addAll(dependencies);
      return this;
   }

   public Set<String> getDefaultBundles() {
      return defaultBundles;
   }

   public GeneratedServiceConfigDto addDefaultBundles(Collection<String> bundles) {
      this.defaultBundles.addAll(bundles);
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
