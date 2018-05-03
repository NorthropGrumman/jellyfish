package com.ngc.seaside.jellyfish.sonarqube.rules;

import com.ngc.seaside.jellyfish.sonarqube.languages.SystemDescriptorLanguage;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class SystemDescriptorRulesDefinition implements RulesDefinition {

   private static final Logger LOGGER = Loggers.get(SystemDescriptorRulesDefinition.class);

   public static final String REPOSITORY = "system-descriptor-rules";

   public static final RuleKey SYNTAX_ERRORS = RuleKey.of(REPOSITORY, "syntax-errors");
   public static final RuleKey SYNTAX_WARNINGS = RuleKey.of(REPOSITORY, "syntax-warnings");

   @Override
   public void define(Context context) {
      NewRepository repository = context.createRepository(REPOSITORY, SystemDescriptorLanguage.KEY)
            .setName("SystemDescriptorAnalyzer");
      addRules(repository);
      repository.done();
      LOGGER.info("Successfully installed System Descriptor rules repository named {}.", REPOSITORY);
   }

   private void addRules(NewRepository repository) {
      NewRule syntaxErrors = repository.createRule(SYNTAX_ERRORS.rule())
            .setName("System Descriptor Syntax Error")
            .setHtmlDescription("Used when a syntax error is raised for an SD file.")
            .setStatus(RuleStatus.BETA)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MAJOR);
      syntaxErrors.setDebtRemediationFunction(syntaxErrors.debtRemediationFunctions().constantPerIssue("5 min"));
   }
}
