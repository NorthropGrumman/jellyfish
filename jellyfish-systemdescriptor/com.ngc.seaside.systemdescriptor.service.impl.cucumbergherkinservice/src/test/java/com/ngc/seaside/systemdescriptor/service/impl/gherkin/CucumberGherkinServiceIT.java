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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.GherkinStepKeyword;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

@RunWith(MockitoJUnitRunner.class)
public class CucumberGherkinServiceIT {

   private CucumberGherkinService service;

   @Mock
   private ILogService logService;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() {
      service = new CucumberGherkinService(logService);
   }

   @Test
   public void testDoesParseValidFeatureFile() {
      setupSdForLinkedModel();
      IScenario simpleSdScenario = systemDescriptor.findModel("b", "MyModel")
            .get()
            .getScenarios()
            .getByName("simple")
            .get();

      IGherkinParsingResult result = service.parseRecursively(Paths.get("build", "resources", "test", "valid"),
                                                              systemDescriptor);
      assertTrue("parsing should have been successful!",
                 result.isSuccessful());
      assertTrue("no issues should be reported!",
                 result.getIssues().isEmpty());

      Optional<IFeature> optional = result.findFeature(simpleSdScenario);
      assertTrue("did not find feature!",
                 optional.isPresent());
      IFeature feature = optional.get();
      assertEquals("SD scenario not set on feature!",
                   simpleSdScenario,
                   feature.getModelScenario().orElse(null));
      assertEquals("short description not correct!",
                   "A simple example",
                   feature.getShortDescription());
      assertEquals("description not correct!",
                   "   This is a longer description",
                   feature.getDescription());
      assertEquals("fully qualified name not correct!",
                   "b.MyModel.simple",
                   feature.getFullyQualifiedName());

      IGherkinScenario outline = feature.getScenarios().getByName("Say hello to lots of people").get();
      assertTrue("tag not correct!",
                 outline.hasTag("example2"));
      assertEquals("wrong number of steps!",
                   4,
                   outline.getSteps().size());
      assertTrue("should be an outline!",
                 outline.isOutline());

      assertTrue("should have a background!",
                 outline.getBackground().isPresent());
      IGherkinStep step = outline.getBackground().get().getSteps().get(0);
      assertEquals("keyword not correct!",
                   GherkinStepKeyword.GIVEN,
                   step.getKeyword());
      assertEquals("step not correct!",
                   "the room has been built",
                   step.getContent());

      step = outline.getWhens().get(0);
      assertEquals("step not correct!",
                   "<person> enters the \"room\"",
                   step.getContent());

      IGherkinTable table = outline.getExamples().get();
      assertEquals("header not correct!",
                   table.getHeader().get().getCells().get(0).getValue(),
                   "Person");
      assertEquals("header not correct!",
                   table.getHeader().get().getCells().get(1).getValue(),
                   "Greeting");
      assertEquals("table not correct!",
                   "Adam",
                   table.getRows().get(0).getCell("Person").get().getValue());
      assertEquals("table not correct!",
                   "Hello",
                   table.getRows().get(0).getCell("Greeting").get().getValue());
   }

   @Test
   public void testDoesParseValidFeatureFileWithNoLinkedModel() {
      IGherkinParsingResult result = service.parseRecursively(Paths.get("build", "resources", "test", "valid"),
                                                              systemDescriptor);

      assertTrue("parsing should have been successful!",
                 result.isSuccessful());
      assertTrue("no issues should be reported!",
                 result.getIssues().isEmpty());
      assertEquals("did not parse all features!",
                   1,
                   result.getFeatures().size());
      assertFalse("SD scenario should not be present!",
                  result.getFeatures().iterator().next().getModelScenario().isPresent());
   }

   @Test
   public void testDoesParseInvalidFeatureFile() {
      setupSdForLinkedModel();

      IGherkinParsingResult result = service.parseRecursively(Paths.get("build", "resources", "test", "invalid"),
                                                              systemDescriptor);

      assertFalse("parsing should have not been successful!",
                  result.isSuccessful());
      assertEquals("should have parsing issue!",
                   1,
                   result.getIssues().size());

      IParsingIssue issue = result.getIssues().iterator().next();
      assertEquals("severity not correct!",
                   Severity.ERROR,
                   issue.getSeverity());
      assertEquals("path not correct!",
                   Paths.get("build", "resources", "test", "invalid", "a", "MyModel.invalid.feature"),
                   issue.getLocation().getPath());
      assertEquals("line number not correct!",
                   41,
                   issue.getLocation().getLineNumber());
      assertEquals("column not correct!",
                   10,
                   issue.getLocation().getColumn());
   }

   private void setupSdForLinkedModel() {
      IModel model = mock(IModel.class);
      IScenario scenario = mock(IScenario.class);
      NamedChildCollection<IModel, IScenario> scenarios = new NamedChildCollection<>();
      when(scenario.getName()).thenReturn("simple");
      scenarios.add(scenario);
      when(model.getScenarios()).thenReturn(scenarios);
      when(systemDescriptor.findModel("b", "MyModel")).thenReturn(Optional.of(model));
   }

}
