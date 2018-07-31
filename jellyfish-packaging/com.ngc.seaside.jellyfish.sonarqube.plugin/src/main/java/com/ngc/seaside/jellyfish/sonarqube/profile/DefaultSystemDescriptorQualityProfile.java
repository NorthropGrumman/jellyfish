package com.ngc.seaside.jellyfish.sonarqube.profile;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * The default quality profile configured for the SD language.
 */
public class DefaultSystemDescriptorQualityProfile implements BuiltInQualityProfilesDefinition {

   /**
    * The name of the default quality profile associated with SD language.
    */
   public static final String NAME = "Default SD Rules";

   private static final Logger LOGGER = Loggers.get(DefaultSystemDescriptorQualityProfile.class);

   private final SystemDescriptorRulesDefinition rules;

   public DefaultSystemDescriptorQualityProfile(SystemDescriptorRulesDefinition rules) {
      this.rules = rules;
   }

   @Override
   public void define(Context context) {
      NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(NAME, SystemDescriptorLanguage.KEY);
      profile.setDefault(true);
      addRules(profile);
      profile.done();

      LOGGER.info("Successfully installed default SD quality profile named {}.", NAME);
   }

   private void addRules(NewBuiltInQualityProfile profile) {
      // Add all the rules in the default repository.  Note that we can only add rules that are in the repository,
      // so if you manually add a rule key, make sure that the rule is associated with some repository.
      rules.getRules().forEach(r -> profile.activateRule(r.getKey().repository(),
                                                         r.getKey().rule()));
   }
}
