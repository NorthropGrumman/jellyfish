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

   /**
    * Contains all rules used by the System Descriptor language.
    */
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
