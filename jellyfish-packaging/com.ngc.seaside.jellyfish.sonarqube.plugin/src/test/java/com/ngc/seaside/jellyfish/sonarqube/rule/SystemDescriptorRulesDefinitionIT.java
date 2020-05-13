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

import com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs.InputsOutputsFindingTypes;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType.Severity;
import com.ngc.seaside.jellyfish.sonarqube.extension.DefaultJellyfishModuleFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SystemDescriptorRulesDefinitionIT {

   private SystemDescriptorRulesDefinition rules;

   @Before
   public void setup() {
      rules = new SystemDescriptorRulesDefinition(new DefaultJellyfishModuleFactory());
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
