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
