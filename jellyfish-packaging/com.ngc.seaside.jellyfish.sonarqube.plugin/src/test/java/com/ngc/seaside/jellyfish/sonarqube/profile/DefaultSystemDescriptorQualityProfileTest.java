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
package com.ngc.seaside.jellyfish.sonarqube.profile;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.rule.AbstractRule;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSystemDescriptorQualityProfileTest {

   private DefaultSystemDescriptorQualityProfile profile;

   @Mock
   private SystemDescriptorRulesDefinition rules;

   @Mock
   private BuiltInQualityProfilesDefinition.Context context;

   @Mock
   private BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile newProfileInstance;

   @Before
   public void setup() {
      when(context.createBuiltInQualityProfile(DefaultSystemDescriptorQualityProfile.NAME,
                                               SystemDescriptorLanguage.KEY))
            .thenReturn(newProfileInstance);
      profile = new DefaultSystemDescriptorQualityProfile(rules);
   }

   @Test
   public void testDoesConfigureProfile() {
      RuleKey key = RuleKey.of("repo", "key");
      AbstractRule rule = mock(AbstractRule.class);
      when(rule.getKey()).thenReturn(key);
      when(rules.getRules()).thenReturn(Collections.singleton(rule));

      profile.define(context);

      rules.getRules()
            .forEach(r -> verify(newProfileInstance).activateRule(r.getKey().repository(),
                                                                  r.getKey().rule()));
      verify(newProfileInstance).setDefault(true);
      verify(newProfileInstance).done();
   }
}
