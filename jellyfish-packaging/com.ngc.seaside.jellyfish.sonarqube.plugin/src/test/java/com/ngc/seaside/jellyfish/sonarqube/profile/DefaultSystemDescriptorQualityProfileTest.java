package com.ngc.seaside.jellyfish.sonarqube.profile;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

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

   @Ignore("Work for SEA18-510 should get this test to pass again")
   @Test
   public void testDoesConfigureProfile() {
      profile.define(context);

      // TODO TH: get this test to pass
//      SystemDescriptorRulesDefinition.getDefaultRules()
//            .forEach(r -> verify(newProfileInstance).activateRule(r.getKey().repository(),
//                                                                  r.getKey().rule()));
      verify(newProfileInstance).setDefault(true);
      verify(newProfileInstance).done();
   }
}
