package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.rules.SystemDescriptorSensor;
import com.ngc.seaside.jellyfish.sonarqube.languages.SystemDescriptorLanguage;

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
                   2);
      assertTrue("did not register language extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorLanguage.class));
      assertTrue("did not register sensor extension for System Descriptor!",
                 c.getExtensions().contains(SystemDescriptorSensor.class));
   }
}
