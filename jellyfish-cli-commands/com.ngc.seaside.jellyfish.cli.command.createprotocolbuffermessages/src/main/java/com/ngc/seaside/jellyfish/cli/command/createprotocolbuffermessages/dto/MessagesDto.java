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
package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

   /**
    *
    * @param exportedPackages of the message
    * @return A MessageDTO
    */
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
}
