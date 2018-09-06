/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FindingTypeRuleAdapterTest extends AbstractRuleTestBase {

   private FindingTypeRuleAdapter findingTypeRuleAdapter;

   @Mock
   private ISystemDescriptorFindingType findingType;

   @Test
   public void testDoesAdaptFindingTypeToRule() {
      when(findingType.getId()).thenReturn("my-finding");
      when(findingType.getDescription()).thenReturn("hello world");
      when(findingType.getSeverity()).thenReturn(ISystemDescriptorFindingType.Severity.ERROR);

      IReportingOutputService reportingOutputService = mock(IReportingOutputService.class);
      when(reportingOutputService.convert("hello world")).thenReturn("<p>hello world</p>");

      findingTypeRuleAdapter = new FindingTypeRuleAdapter(findingType);
      assertEquals("did not return the finding type!",
                   findingTypeRuleAdapter.getFindingType(),
                   findingType);

      findingTypeRuleAdapter.setReportingOutputService(reportingOutputService);
      findingTypeRuleAdapter.configure(newRuleInstance);
      verify(newRuleInstance).setName(findingType.getId());
      verify(newRuleInstance).setHtmlDescription(reportingOutputService.convert(findingType.getDescription()));
      verify(newRuleInstance).setSeverity(Severity.MAJOR);
      verify(newRuleInstance).setStatus(RuleStatus.READY);
      verify(newRuleInstance).setType(RuleType.CODE_SMELL);
      verify(newRuleInstance).setDebtRemediationFunction(any());
   }
}
