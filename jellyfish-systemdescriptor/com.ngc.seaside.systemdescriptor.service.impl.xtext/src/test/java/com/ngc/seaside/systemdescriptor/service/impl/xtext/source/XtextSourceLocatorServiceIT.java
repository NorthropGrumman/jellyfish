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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.IGherkinUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XtextSourceLocatorServiceIT {

   private static ISourceLocatorService service;
   private static ISystemDescriptor sd;
   private static ISystemDescriptor zippedSd;

   @SuppressWarnings("deprecation")
   @BeforeClass
   public static void setup() {
      TerminalsStandaloneSetup.doSetup();
      new SystemDescriptorStandaloneSetup().register(InjectorTestFactory.getSharedInstance());
      service = InjectorTestFactory.getSharedInstance().getInstance(ISourceLocatorService.class);
      ISystemDescriptorService sdService =
               InjectorTestFactory.getSharedInstance().getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseProject(Paths.get("src", "test", "resources", "valid-project"));
      assertTrue("did not parse project!", result.isSuccessful());
      sd = result.getSystemDescriptor();
      result = sdService
               .parseFiles(Collections.singleton(Paths.get("src", "test", "resources", "valid-zipped-project.zip")));
      assertTrue("did not parse project!", result.isSuccessful());
      zippedSd = result.getSystemDescriptor();
   }

   @Test
   public void testZippedData() {
      IData data = zippedSd.findData("com.Test").get();
      testLocation(data, "Test.sd", 3, 6, 4);
   }

   @Test
   public void testModel() {
      String filename = "Alarm.sd";
      IModel alarm = sd.findModel("clocks.models.Alarm").get();
      testLocation(alarm, filename, 22, 7, 5);
      IDataReferenceField input = alarm.getInputs().getByName("currentTime").get();
      testLocation(input, filename, 33, 13, 11);
      IModelReferenceField requires = alarm.getRequiredModels().getByName("speaker").get();
      testLocation(requires, filename, 29, 17, 7);
      IScenario scenario = alarm.getScenarios().getByName("triggerAlerts").get();
      testLocation(scenario, filename, 37, 15, 13);
   }

   @Test
   public void testData() {
      String filename = "Time.sd";
      IData time = sd.findData("clocks.datatypes.Time").get();
      testLocation(time, filename, 19, 6, 4);
      IDataField field = time.getFields().getByName("hour").get();
      testLocation(field, filename, 25, 7, 4);
   }

   @Test
   public void testGherkin() {
      IGherkinUnwrappable<?> gherkin = mock(IGherkinUnwrappable.class);
      when(gherkin.getPath()).thenReturn(Paths.get("foo.feature"));
      when(gherkin.getLineNumber()).thenReturn(100);
      when(gherkin.getColumn()).thenReturn(10);
      testLocation(gherkin, "foo.feature", 100, 10, -1);
   }

   private static void testLocation(Object o, String filename, int line, int column, int length) {
      ISourceLocation location = service.getLocation(o, false);
      assertEquals(filename, location.getPath().getFileName().toString());
      assertEquals(line, location.getLineNumber());
      assertEquals(column, location.getColumn());
      assertEquals(length, location.getLength());
   }

}
