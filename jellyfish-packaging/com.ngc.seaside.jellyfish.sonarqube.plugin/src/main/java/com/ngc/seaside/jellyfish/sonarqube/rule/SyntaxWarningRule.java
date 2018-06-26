package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * A generic rule that for SD syntax warnings.
 */
public class SyntaxWarningRule extends AbstractRule {

   /**
    * The key for this rule.
    */
   public static final RuleKey KEY = RuleKey.of(SystemDescriptorRulesDefinition.REPOSITORY_KEY, "syntax-warning");

   /**
    * Creates a new rule.
    */
   public SyntaxWarningRule() {
      super(KEY);
   }

   @Override
   protected void configure(RulesDefinition.NewRule rule) {
      rule.setName("System Descriptor Syntax Warning")
            .setHtmlDescription("Used when a syntax warning is raised for an SD file.")
            .setStatus(RuleStatus.BETA)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MAJOR);
      rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("5 min"));
   }
}
