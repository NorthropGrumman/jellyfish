package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * A generic rule that for SD syntax errors.
 */
public class SyntaxErrorRule extends AbstractRule {

   /**
    * The key for this rule.
    */
   public static final RuleKey KEY = RuleKey.of(SystemDescriptorRulesDefinition.REPOSITORY_KEY, "syntax-error");

   /**
    * Creates a new rule.
    */
   public SyntaxErrorRule() {
      super(KEY);
   }

   @Override
   protected void configure(RulesDefinition.NewRule rule) {
      rule.setName("System Descriptor Syntax Error")
            .setHtmlDescription("Used when a syntax error is raised for an SD file.")
            .setStatus(RuleStatus.BETA)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MAJOR);
      rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("5 min"));
   }
}
