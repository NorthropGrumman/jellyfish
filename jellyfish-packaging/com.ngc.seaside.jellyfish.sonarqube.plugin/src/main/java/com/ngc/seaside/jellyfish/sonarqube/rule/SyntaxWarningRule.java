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
