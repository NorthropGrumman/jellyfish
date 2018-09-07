package com.ngc.seaside.jellyfish.utilities.command;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

/**
 * A type that is added as a parameter when unpacking a template.  The parameter will have the name {@link
 * AbstractJellyfishCommand#BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE}.
 */
class BuildManagementHelper {

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
