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
