package com.ngc.seaside.jellyfish.sonarqube.properties;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Properties that can be configured for the System Descriptor language.  Many of these properties can be configured
 * per project.  The properties can be set in the {@code build.gradle} file of the project being scanned.  For example,
 * this configuration can be used to set the value of the {@code sonar.jellyfish.cli.extraArguments} property:
 * <pre>{@code
 * sonarqube {
 *   properties {
 *     properties['sonar.jellyfish.cli.extraArguments'] = 'model=com.my.Model'
 *   }
 * }
 * }</pre>
 */
public class SystemDescriptorProperties {

   /**
    * The key of the property that contains the analysis to run when running Jellyfish during the scan.  This is a
    * multi-value property.
    */
   public static String JELLYFISH_ANALYSIS_KEY = "sonar.jellyfish.cli.analysis";

   /**
    * The key of the property that contains additional arguments to include when running Jellyfish.  This is a
    * multi-value property.
    */
   public static String JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY = "sonar.jellyfish.cli.extraArguments";

   /**
    * The multi-value project property that controls the analysis to run when scanning a project.
    */
   private static final PropertyDefinition JELLYFISH_ANALYSIS =
         PropertyDefinition.builder(JELLYFISH_ANALYSIS_KEY)
               .name("Jellyfish Analysis")
               .description("The names of the analysis to run when running the scan.")
               .multiValues(true)
               .onQualifiers(Qualifiers.PROJECT)
               .build();

   /**
    * The multi-value project property that controls additional arguments to pass to Jellyfish when running the scan.
    */
   private static final PropertyDefinition JELLYFISH_CLI_EXTRA_ARGUMENTS =
         PropertyDefinition.builder(JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY)
               .name("Extra Jellyfish CLI Arguments")
               .description("Additional arguments to use when running Jellyfish during the scan.")
               .multiValues(true)
               .onQualifiers(Qualifiers.PROJECT)
               .build();

   private SystemDescriptorProperties() {
   }

   /**
    * Gets all System Descriptor related properties.
    */
   public static List<PropertyDefinition> getProperties() {
      return Arrays.asList(JELLYFISH_ANALYSIS, JELLYFISH_CLI_EXTRA_ARGUMENTS);
   }
}
