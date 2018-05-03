package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Base class for tests of {@code AbstractRule}s.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class AbstractRuleTestBase {

   /**
    * The rule to use in the test when {@link AbstractRule#configure(RulesDefinition.NewRule)} is called.
    */
   @Mock
   protected RulesDefinition.NewRule newRuleInstance;

   @Mock
   private RulesDefinition.DebtRemediationFunctions debtFunctions;

   @Mock
   private DebtRemediationFunction debtFunction;

   @Before
   public void setup() {
      when(debtFunctions.constantPerIssue(anyString())).thenReturn(debtFunction);
      when(debtFunctions.linear(anyString())).thenReturn(debtFunction);
      when(debtFunctions.linearWithOffset(anyString(), anyString())).thenReturn(debtFunction);

      when(newRuleInstance.setName(anyString())).thenReturn(newRuleInstance);
      when(newRuleInstance.setHtmlDescription(anyString())).thenReturn(newRuleInstance);
      when(newRuleInstance.setType(any(RuleType.class))).thenReturn(newRuleInstance);
      when(newRuleInstance.setStatus(any(RuleStatus.class))).thenReturn(newRuleInstance);
      when(newRuleInstance.setSeverity(anyString())).thenReturn(newRuleInstance);
      when(newRuleInstance.setDebtRemediationFunction(any(DebtRemediationFunction.class))).thenReturn(newRuleInstance);
      when(newRuleInstance.debtRemediationFunctions()).thenReturn(debtFunctions);
   }

   /**
    * Asserts that the rule actually set all the required values when it configures itself.
    */
   protected void assertRuleConfigured() {
      verify(newRuleInstance).setName(anyString());
      verify(newRuleInstance).setHtmlDescription(anyString());
      verify(newRuleInstance).setType(any(RuleType.class));
      verify(newRuleInstance).setStatus(any(RuleStatus.class));
      verify(newRuleInstance).setSeverity(anyString());
      verify(newRuleInstance).setDebtRemediationFunction(any(DebtRemediationFunction.class));
   }
}
