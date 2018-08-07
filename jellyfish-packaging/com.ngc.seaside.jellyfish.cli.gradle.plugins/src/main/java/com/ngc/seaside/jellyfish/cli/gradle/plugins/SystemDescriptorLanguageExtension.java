package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import com.ngc.seaside.jellyfish.cli.gradle.RuntimeProperties;

import java.util.Collections;
import java.util.Set;

/**
 * This extension is used to configure the SD language itself during a Gradle build.
 */
public class SystemDescriptorLanguageExtension {

   /**
    * The default name of this extension.
    */
   public static final String EXTENSION_NAME = "sdLang";

   /**
    * The artifact ID of the ZIP that contains the default System Descriptor language constructs.
    */
   private static final String SD_LANG_ARTIFACT_ID = "systemdescriptor.lang";

   /**
    * If true, include the SD language project as a dependency.
    */
   private boolean includeDefaultLanguageDependencies = true;

   /**
    * Gets the default language dependencies that should be included in an SD project.
    *
    * @return the default language dependencies that should be included in an SD project
    */
   public Set<String> getDefaultLanguageDependencies() {
      // Some folks might not want to include the default version of the lang artfacts, so let them opt out.  This
      // "voids their warranty" if they start mixing different versions of the language together.
      return includeDefaultLanguageDependencies ? Collections.singleton(String.format("%s:%s:%s",
                                                                                      RuntimeProperties.getGroup(),
                                                                                      SD_LANG_ARTIFACT_ID,
                                                                                      RuntimeProperties.getVersion()))
                                                : Collections.emptySet();
   }

   /**
    * If true, the default language artifacts will be automatically included as dependencies for a project.  The version
    * of the artifacts will match the current version of Jellyfish.
    *
    * @return true to include the language's default artifacts, false otherwise
    */
   public boolean isIncludeDefaultLanguageDependencies() {
      return includeDefaultLanguageDependencies;
   }

   /**
    * Sets if the default language artifacts will be automatically included as dependencies for a project.
    *
    * @param includeLangDependencies if the default language artifacts will be automatically included as dependencies
    *                                for a project
    * @return this extension
    */
   public SystemDescriptorLanguageExtension setIncludeDefaultLanguageDependencies(boolean includeLangDependencies) {
      this.includeDefaultLanguageDependencies = includeLangDependencies;
      return this;
   }
}
