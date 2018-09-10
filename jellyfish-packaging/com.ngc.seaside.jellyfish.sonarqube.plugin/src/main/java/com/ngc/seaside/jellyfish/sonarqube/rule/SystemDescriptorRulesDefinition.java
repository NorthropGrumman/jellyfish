/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.sonarqube.rule;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.Jellyfish;
import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.module.JellyfishSonarqubePluginModule;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Creates a new repository with the key {@link #REPOSITORY_KEY} and adds all the default System Descritpor rules to it.
 * Rules configured via Jellyfish are also included.
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
    * Contains a mapping from finding types to the rule key for the adapter for the finding type.
    */
   private final Map<ISystemDescriptorFindingType, RuleKey> ruleKeys = new HashMap<>();

   /**
    * Contains all rules added to the repository.  This includes all rules manually configured as well as rules that
    * were discovered as implementations of {@link ISystemDescriptorFindingType}.
    */
   private final Set<AbstractRule> rules = new HashSet<>();

   /**
    * Creates a new {@code SystemDescriptorRulesDefinition}.
    */
   public SystemDescriptorRulesDefinition() {
      findRules();
   }

   @Override
   public void define(Context context) {
      NewRepository repository = context.createRepository(REPOSITORY_KEY, SystemDescriptorLanguage.KEY)
            .setName(REPOSITORY_NAME);
      getRules().forEach(r -> r.addRuleToRepository(repository));
      repository.done();
      LOGGER.info("Successfully installed System Descriptor rules in repository named {}.", REPOSITORY_NAME);
   }

   /**
    * Gets all the default rules that will be configured in the repository.
    *
    * @return the default rules that will be configured in the repository
    */
   public Set<AbstractRule> getRules() {
      return Collections.unmodifiableSet(rules);
   }

   /**
    * Gets the rule key for the given finding type.
    *
    * @param findingType the finding type to get the rule key for
    * @return the rule key
    * @throws IllegalArgumentException if no rule key could be found for the finding type
    */
   public RuleKey getRuleKey(ISystemDescriptorFindingType findingType) {
      Preconditions.checkNotNull(findingType, "findingType may not be null!");
      RuleKey key = ruleKeys.get(findingType);
      Preconditions.checkArgument(key != null,
                                  "findingType not declared in a Guice module!  Make sure %s is declared with a"
                                  + " multibinder in a Guice module!",
                                  findingType);
      return key;
   }

   /**
    * Finds all the rules that are manually registered as well as the rules that are registered at runtime.
    */
   private void findRules() {
      // Add any manually configured rules here.
      rules.add(new SyntaxWarningRule());

      // Just run the help command with no arguments because we don't actually want to run Jellyfish.  We just want
      // to get the injector so we can find all the implementations of ISystemDescriptorFindingType.  Create the
      // Jellyfish execution service using a module that is configured to suppress logging.  If we use normal logging
      // the output of the help command shows up the Sonarqube server logs which is weird.
      IJellyfishExecution result = Jellyfish
            .getService()
            .run("help",
                 Collections.emptyList(),
                 Collections.singleton(JellyfishSonarqubePluginModule.withSuppressedLogging()));
      // Get all the finding types and adapt them to Sonarqube rules.
      result.getInjector()
            // Get impls by asking Guice for an instance of FindingTypeHolder which requires all the finding types
            .getInstance(FindingTypeHolder.class)
            .findingTypes
            .stream()
            // Adapt the finding types to rules.
            .map(FindingTypeRuleAdapter::new)
               .forEach(rule -> {  // Weird indent for Checkstyle.
                  rule.setReportingOutputService(result.getInjector().getInstance(IReportingOutputService.class));
                  // Add the rule to the complete set which includes the manual rules.
                  rules.add(rule);
                  // Add a mapping from finding type to rule key.
                  ruleKeys.put(rule.getFindingType(), rule.getKey());
               });
   }

   /**
    * A value holder used with an Injector get all instances of {@code ISystemDescriptorFindingType} injected.
    */
   static class FindingTypeHolder {

      /**
       * The finding types as provided by the Injector.
       */
      private final Set<ISystemDescriptorFindingType> findingTypes;

      /**
       * Used by Guice to inject all the finding types.
       *
       * @param findingTypes the finding types to register as Sonarqube rules.
       */
      @Inject
      FindingTypeHolder(Set<ISystemDescriptorFindingType> findingTypes) {
         this.findingTypes = findingTypes;
      }
   }
}
