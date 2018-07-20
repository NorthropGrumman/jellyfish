package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzeInputsOutputsCommandTest {

   private AnalyzeInputsOutputsCommand command;

   @Mock
   private ILogService logService;

   @Mock
   private IAnalysisService analysisService;

   @Mock
   private ISourceLocatorService sourceLocatorService;

   @Before
   public void setup() {
      command = new AnalyzeInputsOutputsCommand();
      command.setLogService(logService);
      command.setAnalysisService(analysisService);
      command.setSourceLocatorService(sourceLocatorService);
      command.activate();
   }

   @Test
   public void testDoesCreateFindingIfModelHasNoInputs() {
      Model model = new Model("com.Foo");
      model.addInput(new DataReferenceField("field1"));

      ISourceLocation location = mock(ISourceLocation.class);
      when(sourceLocatorService.getLocation(model, false)).thenReturn(location);

      command.analyzeModel(model);

      ArgumentCaptor<SystemDescriptorFinding<?>> captor = ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService).addFinding(captor.capture());
      assertNotNull("message not set!",
                    captor.getValue().getMessage());
      assertEquals("location not correct!",
                   location,
                   captor.getValue().getLocation().orElse(null));
      assertEquals("finding type not correct!",
                   InputsOutputsFindingTypes.INPUTS_WITH_NO_OUTPUTS,
                   captor.getValue().getType());
   }

   @Test
   public void testDoesNotCreateFindingIfModelHasOutput() {
      Model model = new Model("com.Foo");
      model.addInput(new DataReferenceField("field1"));
      model.addOutput(new DataReferenceField("field2"));
      command.analyzeModel(model);
      verify(analysisService, never()).addFinding(any());
   }

   @Test
   public void testDoesNotCreateFindingIfModelHasNoInputs() {
      Model model = new Model("com.Foo");
      command.analyzeModel(model);
      verify(analysisService, never()).addFinding(any());
   }
}
