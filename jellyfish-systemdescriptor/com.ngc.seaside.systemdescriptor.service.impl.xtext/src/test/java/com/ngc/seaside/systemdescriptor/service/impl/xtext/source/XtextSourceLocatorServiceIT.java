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
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XtextSourceLocatorServiceIT {

   private static ISourceLocatorService service;
   private static ISystemDescriptor sd;

   @BeforeClass
   public static void setup() {
      TerminalsStandaloneSetup.doSetup();
      new SystemDescriptorStandaloneSetup().register(InjectorTestFactory.getSharedInstance());
      service = InjectorTestFactory.getSharedInstance().getInstance(ISourceLocatorService.class);
      ISystemDescriptorService sdService =
               InjectorTestFactory.getSharedInstance().getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!", result.isSuccessful());
      sd = result.getSystemDescriptor();
   }

   @Test
   public void testModel() {
      String filename = "Alarm.sd";
      IModel alarm = sd.findModel("clocks.models.Alarm").get();
      testLocation(alarm, filename, 6, 7, 5);
      IDataReferenceField input = alarm.getInputs().getByName("currentTime").get();
      testLocation(input, filename, 17, 13, 11);
      IModelReferenceField requires = alarm.getRequiredModels().getByName("speaker").get();
      testLocation(requires, filename, 13, 17, 7);
      IScenario scenario = alarm.getScenarios().getByName("triggerAlerts").get();
      testLocation(scenario, filename, 21, 15, 13);
   }

   @Test
   public void testData() {
      String filename = "Time.sd";
      IData time = sd.findData("clocks.datatypes.Time").get();
      testLocation(time, filename, 3, 6, 4);
      IDataField field = time.getFields().getByName("hour").get();
      testLocation(field, filename, 9, 7, 4);
   }

   private static void testLocation(Object o, String filename, int line, int column, int length) {
      ISourceLocation location = service.getLocation(o, false);
      assertEquals(filename, location.getPath().getFileName().toString());
      assertEquals(line, location.getLineNumber());
      assertEquals(column, location.getColumn());
      assertEquals(length, location.getLength());
   }

}
