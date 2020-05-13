/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
