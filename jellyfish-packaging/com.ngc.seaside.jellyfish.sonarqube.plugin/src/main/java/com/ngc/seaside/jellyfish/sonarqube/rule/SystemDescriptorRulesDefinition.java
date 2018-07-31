package com.ngc.seaside.jellyfish.sonarqube.rule;

import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Creates a new repository with the key {@link #REPOSITORY_KEY} and adds all the default SD rules to it.
 */
@Singleton
public class SystemDescriptorRulesDefinition implements RulesDefinition {

   /**
    * The human readable name of the repository that contains the default rules for the SD language.  Do not use this
    * value when creating rules.  Use {@link #REPOSITORY_KEY} instead.
    */
   public static final String REPOSITORY_NAME = "SystemDescriptorAnalyzer";

   /**
    * The key of the repository that contains the default rules for the SD language.  Use this value as the first
    * argument to {@link RuleKey#of(String, String)} when creating new rules.
    */
   public static final String REPOSITORY_KEY = "system-descriptor-rules";

   private static final Logger LOGGER = Loggers.get(SystemDescriptorRulesDefinition.class);

   /**
    * Contains all the default rules in the repository configured by this definition.
    */
   // TODO TH: remove this field.
   private static final Set<AbstractRule> DEFAULT_RULES;

   static {
      Set<AbstractRule> rules = new TreeSet<>();
      // Add rules here.
      rules.add(new SyntaxWarningRule());
      DEFAULT_RULES = Collections.unmodifiableSet(rules);
   }

   @Override
   public void define(Context context) {
      NewRepository repository = context.createRepository(REPOSITORY_KEY, SystemDescriptorLanguage.KEY)
            .setName(REPOSITORY_NAME);
      addRules(repository);
      repository.done();
      LOGGER.info("Successfully installed System Descriptor rules in repository named {}.", REPOSITORY_NAME);
   }

   /**
    * Gets all the default rules that will be configured in the repository.
    *
    * @return the default rules that will be configured in the repository
    */
   public Set<AbstractRule> getRules() {
      // TODO TH: implement this, this is just temporary.
      return Collections.emptySet();
   }

   private void addRules(NewRepository repository) {
      // Create an instance of Jellyfish and just run the help command.  Get all the impls of
      // ISystemDescriptorFindingType.
      DEFAULT_RULES.forEach(r -> r.addRuleToRepository(repository));
   }

//   private static class FindingTypeHolder {
//      private final Set<IFindingType>
//   }
}
