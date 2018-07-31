package com.ngc.seaside.jellyfish.sonarqube.rule;

import com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs.InputsOutputsFindingTypes;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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

      assertEquals("did not mapping ISystemDescriptorFindingTypes to rule key!",
                   inputsOutputsFindingType.get().getKey(),
                   rules.getRuleKey(inputsOutputsFindingType.get().getFindingType()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotAllowUnrecognizedFindingTypes() {
      ISystemDescriptorFindingType findingType = mock(ISystemDescriptorFindingType.class);
      rules.getRuleKey(findingType);
   }
}
