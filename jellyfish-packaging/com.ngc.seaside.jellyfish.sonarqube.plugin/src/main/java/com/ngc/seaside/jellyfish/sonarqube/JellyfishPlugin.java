package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.profile.DefaultSystemDescriptorQualityProfile;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;
import com.ngc.seaside.jellyfish.sonarqube.sensor.SystemDescriptorSensor;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * The main entry point for the Sonarqube Jellyfish plugin.
 */
public class JellyfishPlugin implements Plugin {

   private static final Logger LOGGER = Loggers.get(JellyfishPlugin.class);

   @Override
   public void define(Context c) {
      // Register all extension points here.
      c.addExtension(SystemDescriptorLanguage.class);
      c.addExtension(SystemDescriptorRulesDefinition.class);
      c.addExtension(DefaultSystemDescriptorQualityProfile.class);
      c.addExtension(SystemDescriptorSensor.class);

      LOGGER.info("Jellyfish plugin successfully installed.");
   }
}
