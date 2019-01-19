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
