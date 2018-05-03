package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.profile.DefaultSystemDescriptorQualityProfile;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
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

   @Test
   public void hasCorrectExtensions() {
      JellyfishPlugin p = new JellyfishPlugin();
      SonarRuntime r = SonarRuntimeImpl.forSonarQube(Version.create(6, 7), SonarQubeSide.SCANNER);
      Plugin.Context c = new Plugin.Context(r);
      p.define(c);
      assertEquals("did not register all extensions!",
                   c.getExtensions().size(),
                   4);
      assertTrue("did not register language extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorLanguage.class));
      assertTrue("did not register sensor extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorSensor.class));
      assertTrue("did not register rule definition extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorRulesDefinition.class));
      assertTrue("did not register default quality profile extension for System Descriptor!",
                 c.getExtensions().contains(DefaultSystemDescriptorQualityProfile.class));
   }
}
