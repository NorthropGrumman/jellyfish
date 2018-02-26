package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessagesDto {

   private final IBuildManagementService buildManagementService;
   private final IJellyFishCommandOptions options;

   private String projectName;
   private Set<String> exportedPackages;

   public MessagesDto(IBuildManagementService buildManagementService,
                      IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   public String getProjectName() {
      return projectName;
   }

   public MessagesDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public Set<String> getExportedPackages() {
      return exportedPackages;
   }

   public MessagesDto setExportedPackages(Set<String> exportedPackages) {
      exportedPackages.removeIf(str -> {
         List<String> list = Arrays.asList(str.split("\\."));
         for (int n = 1; n < list.size(); n++) {
            String sub = list.subList(0, n).stream().collect(Collectors.joining("."));
            if (exportedPackages.contains(sub)) {
               return true;
            }
         }
         return false;
      });
      this.exportedPackages = exportedPackages;
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
