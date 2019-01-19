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
package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.profile.DefaultSystemDescriptorQualityProfile;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;
import com.ngc.seaside.jellyfish.sonarqube.sensor.SystemDescriptorSensor;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JellyfishPluginTest {

   @SuppressWarnings({"unchecked"})
   @Test
   public void hasCorrectExtensions() {
      JellyfishPlugin p = new JellyfishPlugin();
      SonarRuntime r = SonarRuntimeImpl.forSonarQube(Version.create(6, 7), SonarQubeSide.SCANNER);
      Plugin.Context c = new Plugin.Context(r);
      p.define(c);
      assertTrue("did not register language extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorLanguage.class));
      assertTrue("did not register sensor extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorSensor.class));
      assertTrue("did not register rule definition extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorRulesDefinition.class));
      assertTrue("did not register default quality profile extension for System Descriptor!",
                 c.getExtensions().contains(DefaultSystemDescriptorQualityProfile.class));
      assertTrue("did not register System Descriptor properties!",
                 c.getExtensions().containsAll(SystemDescriptorProperties.getProperties()));
      assertEquals("registered extra extensions!",
                   7,
                   c.getExtensions().size());
   }
}
