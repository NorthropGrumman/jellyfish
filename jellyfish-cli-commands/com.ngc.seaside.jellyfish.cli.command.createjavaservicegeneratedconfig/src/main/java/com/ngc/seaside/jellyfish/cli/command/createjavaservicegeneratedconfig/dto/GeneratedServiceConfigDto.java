/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
