package com.ngc.seaside.jellyfish.sonarqube.profile;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.rules.SystemDescriptorRulesDefinition;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class DefaultSystemDescriptorQualityProfile implements BuiltInQualityProfilesDefinition {

   private static final Logger LOGGER = Loggers.get(DefaultSystemDescriptorQualityProfile.class);

   public static final String NAME = "Default SD Rules";

   @Override
   public void define(Context context) {
      NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(NAME, SystemDescriptorLanguage.KEY);
      profile.setDefault(true);
      for (RuleKey rule : SystemDescriptorRulesDefinition.Rules.ALL) {
         profile.activateRule(rule.repository(), rule.rule());
      }
      profile.done();

      LOGGER.info("Successfully installed default SD quality profile named {}.", NAME);
   }
}
