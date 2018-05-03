package com.ngc.seaside.jellyfish.sonarqube.rules;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SystemDescriptorRulesDefinition implements RulesDefinition {

   private static final Logger LOGGER = Loggers.get(SystemDescriptorRulesDefinition.class);


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


   @Override
   public void define(Context context) {
      NewRepository repository = context.createRepository(REPOSITORY_KEY, SystemDescriptorLanguage.KEY)
            .setName(REPOSITORY_NAME);
      addRules(repository);
      repository.done();
      LOGGER.info("Successfully installed System Descriptor rules repository named {}.", REPOSITORY_NAME);
   }

   private void addRules(NewRepository repository) {
      NewRule syntaxErrors = repository.createRule(Rules.SYNTAX_ERRORS.rule())
            .setName("System Descriptor Syntax Error")
            .setHtmlDescription("Used when a syntax error is raised for an SD file.")
            .setStatus(RuleStatus.BETA)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MAJOR);
      syntaxErrors.setDebtRemediationFunction(syntaxErrors.debtRemediationFunctions().constantPerIssue("5 min"));

      NewRule syntaxWarnings = repository.createRule(Rules.SYNTAX_WARNINGS.rule())
            .setName("System Descriptor Syntax Warning")
            .setHtmlDescription("Used when a syntax warning is raised for an SD file.")
            .setStatus(RuleStatus.BETA)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MAJOR);
      syntaxWarnings.setDebtRemediationFunction(syntaxErrors.debtRemediationFunctions().constantPerIssue("5 min"));
   }

   public static class Rules {

      private Rules() {
      }

      public static final RuleKey SYNTAX_ERRORS = RuleKey.of(REPOSITORY_KEY, "syntax-errors");
      public static final RuleKey SYNTAX_WARNINGS = RuleKey.of(REPOSITORY_KEY, "syntax-warnings");

      public static final Collection<RuleKey> ALL;

      static {
         Set<RuleKey> rules = new TreeSet<>(Comparator.comparing(RuleKey::rule));
         rules.add(SYNTAX_ERRORS);
         rules.add(SYNTAX_WARNINGS);
         ALL = Collections.unmodifiableSet(rules);
      }
   }
}
