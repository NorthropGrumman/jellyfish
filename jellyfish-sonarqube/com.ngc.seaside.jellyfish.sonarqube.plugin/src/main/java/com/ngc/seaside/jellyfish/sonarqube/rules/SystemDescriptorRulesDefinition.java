package com.ngc.seaside.jellyfish.sonarqube.rules;

import com.ngc.seaside.jellyfish.sonarqube.languages.SystemDescriptorLanguage;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

public class SystemDescriptorRulesDefinition implements RulesDefinition {

   public static final String REPOSITORY = "system-descriptor-rules";

   public static final RuleKey SYNTAX_ERRORS = RuleKey.of(REPOSITORY, "syntax-errors");
   public static final RuleKey SYNTAX_WARNINGS = RuleKey.of(REPOSITORY, "syntax-warnings");

   @Override
   public void define(Context context) {
      NewRepository repository = context.createRepository(REPOSITORY, SystemDescriptorLanguage.NAME)
            .setName("SystemDescriptorAnalyzer");
      addRules(repository);
      repository.done();
   }

   private void addRules(NewRepository repository) {
      NewRule syntaxErrors = repository.createRule(SYNTAX_ERRORS.rule())
            .setName("System Descriptor Syntax Errors")
            .setHtmlDescription("Syntax errors indicate that models files are not valid.")
            .setTags("breaking")
            .setStatus(RuleStatus.BETA)
            .setSeverity(Severity.MAJOR);
      syntaxErrors.setDebtRemediationFunction(syntaxErrors.debtRemediationFunctions().constantPerIssue("1 h"));
   }
}
