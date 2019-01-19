/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
            .setHtmlDescription("<h2>Used when a syntax warning is raised for an SD file.<h2>")
            .setStatus(RuleStatus.READY)
            .setType(RuleType.CODE_SMELL)
            .setSeverity(Severity.MINOR);
      rule.setDebtRemediationFunction(rule.debtRemediationFunctions().constantPerIssue("5 min"));
   }
}
