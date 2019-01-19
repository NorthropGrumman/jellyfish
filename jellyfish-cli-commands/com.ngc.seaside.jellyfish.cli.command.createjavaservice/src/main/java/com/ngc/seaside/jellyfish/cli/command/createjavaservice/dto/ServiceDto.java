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
package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.Set;

public class ServiceDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectDirectoryName;
   private ClassDto service;
   private Set<String> projectDependencies;
   private String interfaze;
   private String baseClass;

   public ServiceDto(IBuildManagementService buildManagementService,
                     IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getProjectDirectoryName() {
      return projectDirectoryName;
   }

   public ServiceDto setProjectDirectoryName(String projectDirectoryName) {
      this.projectDirectoryName = projectDirectoryName;
      return this;
   }

   public ClassDto getService() {
      return service;
   }

   public ServiceDto setService(ClassDto service) {
      this.service = service;
      return this;
   }

   public Set<String> getProjectDependencies() {
      return projectDependencies;
   }

   public ServiceDto setProjectDependencies(Set<String> projectDependencies) {
      this.projectDependencies = projectDependencies;
      return this;
   }

   public String getInterface() {
      return interfaze;
   }

   public ServiceDto setInterface(String interfaze) {
      this.interfaze = interfaze;
      return this;
   }

   public String getBaseClass() {
      return baseClass;
   }

   public ServiceDto setBaseClass(String baseClass) {
      this.baseClass = baseClass;
      return this;
   }

   /**
    *
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
