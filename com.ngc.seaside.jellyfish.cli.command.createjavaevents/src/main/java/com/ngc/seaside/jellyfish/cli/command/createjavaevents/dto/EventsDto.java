package com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto;

import java.util.Set;

public class EventsDto {

   private String projectName;
   private Set<String> exportedPackages;

   public String getProjectName() {
      return projectName;
   }

   public EventsDto setProjectName(String projectName) {
      this.projectName = projectName;
      return this;
   }

   public Set<String> getExportedPackages() {
      return exportedPackages;
   }

   public EventsDto setExportedPackages(Set<String> exportedPackages) {
      this.exportedPackages = exportedPackages;
      return this;
   }

}
