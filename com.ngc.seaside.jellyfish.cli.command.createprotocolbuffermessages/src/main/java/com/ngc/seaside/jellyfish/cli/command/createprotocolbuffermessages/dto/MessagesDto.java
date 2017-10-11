package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto;

import java.util.Set;

public class MessagesDto {

   private String projectName;
   private Set<String> exportedPackages;

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
      this.exportedPackages = exportedPackages;
      return this;
   }

}
