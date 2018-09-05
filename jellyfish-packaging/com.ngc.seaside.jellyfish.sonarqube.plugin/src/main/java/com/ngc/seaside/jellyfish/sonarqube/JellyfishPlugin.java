/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.profile.DefaultSystemDescriptorQualityProfile;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;
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
      c.addExtensions(SystemDescriptorProperties.getProperties());

      LOGGER.info("Jellyfish plugin successfully installed.");
   }
}
