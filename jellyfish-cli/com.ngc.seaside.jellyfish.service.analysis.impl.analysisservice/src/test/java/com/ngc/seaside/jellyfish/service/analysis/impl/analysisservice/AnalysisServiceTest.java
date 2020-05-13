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
package com.ngc.seaside.jellyfish.service.analysis.impl.analysisservice;

import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisServiceTest {

   @Test
   public void testAnalysisService() {
      AnalysisService service = new AnalysisService();

      ISystemDescriptorFindingType type1 = mock(ISystemDescriptorFindingType.class, CALLS_REAL_METHODS);
      ISystemDescriptorFindingType type2 = mock(ISystemDescriptorFindingType.class, CALLS_REAL_METHODS);

      SystemDescriptorFinding<?> finding1 = type1.createFinding("message1", null, 0);
      SystemDescriptorFinding<?> finding2 = type1.createFinding("message2", mock(ISourceLocation.class), 10);
      SystemDescriptorFinding<?> finding3 = type2.createFinding("message3", null, 0);
      SystemDescriptorFinding<?> finding4 = type2.createFinding("message4", mock(ISourceLocation.class), 20);

      service.addFinding(finding1);
      service.addFinding(finding3);

      assertEquals(service.getFindings(), Arrays.asList(finding1, finding3));

      service.addFinding(finding2);
      service.addFinding(finding4);

      assertEquals(service.getFindings(), Arrays.asList(finding1, finding3, finding2, finding4));
   }

}
