package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.dto;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeneratedTestConfigDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectDirectoryName;
   private String baseProjectName;
   private boolean system;

   private Set<String> compileDependencies = new LinkedHashSet<>();
   private Set<String> defaultModules = new LinkedHashSet<>();

   /**
    * Constructor.
    * 
    * @param buildManagementService build management service
    * @param options jellyfish command options
    */
   public GeneratedTestConfigDto(IBuildManagementService buildManagementService,
                                    IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
      this.system = CommonParameters.evaluateBooleanParameter(options.getParameters(),
               CommonParameters.SYSTEM.getName(), false);
   }

   public boolean isSystem() {
      return system;
   }

   public String getBaseProjectName() {
      return baseProjectName;
   }

   public GeneratedTestConfigDto setBaseProjectName(String baseProjectName) {
      this.baseProjectName = baseProjectName;
      return this;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public GeneratedTestConfigDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public Set<String> getCompileDependencies() {
      return compileDependencies;
   }

   public GeneratedTestConfigDto addCompileDependencies(Collection<String> dependencies) {
      this.compileDependencies.addAll(dependencies);
      return this;
   }

   public Set<String> getDefaultModules() {
      return defaultModules;
   }

   public GeneratedTestConfigDto addDefaultModules(Collection<String> bundles) {
      this.defaultModules.addAll(bundles);
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
