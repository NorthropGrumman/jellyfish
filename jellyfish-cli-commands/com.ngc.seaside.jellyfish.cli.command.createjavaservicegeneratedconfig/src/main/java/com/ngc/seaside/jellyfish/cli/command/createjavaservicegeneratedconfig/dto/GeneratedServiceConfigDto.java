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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

import com.ngc.seaside.jellyfish.api.CommonParameters;
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
   private boolean system;

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
      this.system = CommonParameters.evaluateBooleanParameter(options.getParameters(),
               CommonParameters.SYSTEM.getName(), false);
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

   public boolean isSystem() {
      return system;
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
