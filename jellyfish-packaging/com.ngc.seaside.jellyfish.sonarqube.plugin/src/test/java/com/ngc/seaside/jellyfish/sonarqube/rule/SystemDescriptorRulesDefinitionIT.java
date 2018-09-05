package com.ngc.seaside.jellyfish.sonarqube.rule;

import com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs.InputsOutputsFindingTypes;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType.Severity;

import ch.qos.logback.classic.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemDescriptorRulesDefinitionIT {

   private SystemDescriptorRulesDefinition rules;

   @Before
   public void setup() {
      rules = new SystemDescriptorRulesDefinition();
   }

   @Test
   public void testCreateRulesFromFindingTypes() {
      Collection<AbstractRule> allRules = rules.getRules();
      assertTrue("did not register manual rules!",
                 allRules.stream().anyMatch(r -> r instanceof SyntaxWarningRule));

      Optional<FindingTypeRuleAdapter> inputsOutputsFindingType = allRules.stream()
            .filter(r -> r instanceof FindingTypeRuleAdapter)
            .map(FindingTypeRuleAdapter.class::cast)
            .filter(r -> r.getFindingType() instanceof InputsOutputsFindingTypes)
            .findAny();
      
      assertTrue("did not register rule from Jellyfish!",
                 inputsOutputsFindingType.isPresent());

      assertEquals("did not map ISystemDescriptorFindingTypes to rule key!",
                   inputsOutputsFindingType.get().getKey(),
                   rules.getRuleKey(inputsOutputsFindingType.get().getFindingType()));
      
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotAllowUnrecognizedFindingTypes() {
      ISystemDescriptorFindingType findingType = mock(ISystemDescriptorFindingType.class);
      rules.getRuleKey(findingType);
   }

   @Test
   public void testDoesNotReportInfoFinding() {
      Collection<AbstractRule> allRules = rules.getRules();
      for (AbstractRule rule : allRules) {
         if (rule instanceof FindingTypeRuleAdapter) {
            assertNotEquals("Rule contained an info finding", Severity.INFO,
                     ((FindingTypeRuleAdapter) rule).getFindingType().getSeverity());
         }
      }
   }
}
