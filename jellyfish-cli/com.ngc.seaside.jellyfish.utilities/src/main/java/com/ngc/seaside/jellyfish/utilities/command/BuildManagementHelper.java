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
package com.ngc.seaside.jellyfish.utilities.command;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

/**
 * A type that is added as a parameter when unpacking a template.  The parameter will have the name {@link
 * AbstractJellyfishCommand#BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE}.
 */
public class BuildManagementHelper {

   private final IBuildManagementService buildManagementService;

   private final IJellyFishCommandOptions options;

   BuildManagementHelper(IBuildManagementService buildManagementService, IJellyFishCommandOptions options) {
      this.buildManagementService = buildManagementService;
      this.options = options;
   }

   /**
    * Gets a string that describes the given dependency in the format
    * {@code groupId:artifactId:$versionPropertyName}.
    *
    * @return a string that describes the given dependency
    */
   public String getFormattedDependency(String groupAndArtifactId) {
      IBuildDependency dependency = buildManagementService.registerDependency(options, groupAndArtifactId);
      return String.format("%s:%s:$%s",
                           dependency.getGroupId(),
                           dependency.getArtifactId(),
                           dependency.getVersionPropertyName());
   }
}
