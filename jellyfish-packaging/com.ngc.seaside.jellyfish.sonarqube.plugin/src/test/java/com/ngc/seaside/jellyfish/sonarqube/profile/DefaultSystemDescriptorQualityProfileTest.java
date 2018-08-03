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
