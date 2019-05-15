/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinCell;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XtextSourceLocatorServiceFunctionCallsIT {

   private static ISourceLocatorService service;
   private static ISystemDescriptor sd;
   private static IGherkinParsingResult gh;

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

      IGherkinService ghService = InjectorTestFactory.getSharedInstance().getInstance(IGherkinService.class);
      gh = ghService.parseProject(result);
   }

   @Test
   public void testModelLocations() {
      IModel model = sd.findModel("clocks.AlarmClock").get();

      assertText("AlarmClock", service.with(model).calling(m -> m.getName()).getLocation(), "AlarmClock.sd");

      assertText("clocks", service.with(model).calling(m -> m.getParent()).getLocation(), "AlarmClock.sd");

      // Inputs
      assertText("many Time alarmTimes",
               service.with(model).calling(m -> m.getInputs().getByName("alarmTimes")).getLocation(), "AlarmClock.sd");

      assertText("many Time alarmTimes",
               service.with(model).calling(m -> m.getInputs().getByName("alarmTimes").get()).getLocation(),
               "AlarmClock.sd");

      // Metadata
      assertText("\"The top level alarm clock system.  It requires the alarmTimes from some other component.\"",
               service.with(model).calling(m -> m.getMetadata().getJson().get("description")).getLocation(),
               "AlarmClock.sd");

      assertText(
               "system", service.with(model)
                        .calling(m -> m.getMetadata().getJson().getJsonArray("stereotypes").getString(1)).getLocation(),
               "AlarmClock.sd");

      // Parts
      assertText("Timer timer", service.with(model).calling(m -> m.getParts().getByName("timer")).getLocation(),
               "AlarmClock.sd");

      // Requires
      assertText("Speaker speaker",
               service.with(model).calling(m -> m.getRequiredModels().getByName("speaker")).getLocation(),
               "AlarmClock.sd");

   }

   @Test
   public void testDataLocations() {
      IData data = sd.findData("clocks.datatypes.ComplexTime").get();

      assertText("ComplexTime", service.with(data).calling(d -> d.getName()).getLocation(), "ComplexTime.sd");

      assertText("clocks.datatypes", service.with(data).calling(d -> d.getParent()).getLocation(), "ComplexTime.sd");

      assertText("Represents a local time (does not account for timezones).",
               service.with(data).calling(d -> d.getMetadata().getJson().getString("description")).getLocation(),
               "ComplexTime.sd");

      assertText("TimeZone timeZone",
               service.with(data).calling(d -> d.getFields().getByName("timeZone")).getLocation(), "ComplexTime.sd");

      IDataField timeZoneField = data.getFields().getByName("timeZone").get();
      assertText("TimeZone",
               service.with(timeZoneField).calling(f -> f.getReferencedEnumeration()).getLocation(),
               "ComplexTime.sd");

      assertText("TimeZone",
               service.with(timeZoneField).calling(f -> f.getReferencedEnumeration().getName()).getLocation(),
               "TimeZone.sd");

      IDataField hourField = data.getFields().getByName("hour").get();

      assertText("int", service.with(hourField).calling(f -> f.getType()).getLocation(), "ComplexTime.sd");

      assertText("hour", service.with(hourField).calling(f -> f.getName()).getLocation(), "ComplexTime.sd");

      assertText("0",
               service.with(hourField).calling(f -> f.getMetadata().getJson().getJsonObject("validation").get("min"))
                        .getLocation(),
               "ComplexTime.sd");
   }

   @Test
   public void testEnumLocations() {
      IEnumeration enumeration = sd.findEnumeration("clocks.datatypes.TimeZone").get();

      assertText("TimeZone", service.with(enumeration).calling(e -> e.getName()).getLocation(), "TimeZone.sd");

      assertText("clocks.datatypes", service.with(enumeration).calling(e -> e.getParent()).getLocation(),
               "TimeZone.sd");

      assertText("CST", service.with(enumeration).calling(e -> e.getValues().iterator().next()).getLocation(),
               "TimeZone.sd");

      assertText("EST",
               service.with(enumeration).calling(e -> e.getValues().contains("EST")).getLocation(),
               "TimeZone.sd");

      assertText("MST",
               service.with(enumeration).calling(e -> {
                  Iterator<String> itr = e.getValues().iterator();
                  itr.next();
                  return itr.next();
               }).getLocation(),
               "TimeZone.sd");
   }

   @Test
   public void testScenarioLocations() {
      IScenario scenario = sd.findModel("clocks.AlarmClock").get().getScenarios().getByName("beep").get();

      assertText("beep", service.with(scenario).calling(s -> s.getName()).getLocation(), "AlarmClock.sd");

      assertText("value",
               service.with(scenario).calling(s -> s.getMetadata().getJson().getString("example")).getLocation(),
               "AlarmClock.sd");

      assertText("when TODO a b c", service.with(scenario).calling(s -> s.getWhens()).getLocation(), "AlarmClock.sd");

      assertText("TODO a b c", service.with(scenario).calling(s -> s.getGivens().iterator().next()).getLocation(),
               "AlarmClock.sd");

      IScenarioStep step = scenario.getGivens().iterator().next();
      assertText("TODO",
               service.with(step).calling(s -> s.getKeyword()).getLocation(),
               "AlarmClock.sd");

      assertText("a b c",
               service.with(step).calling(s -> s.getParameters()).getLocation(),
               "AlarmClock.sd");

      assertText("a",
               service.with(step).calling(s -> s.getParameters().get(0))
                        .getLocation(),
               "AlarmClock.sd");

      assertText("b c",
               service.with(step).calling(s -> s.getParameters().subList(1, 3))
                        .getLocation(),
               "AlarmClock.sd");
   }

   @Test
   public void testGherkinLocations() {
      IScenario sdScenario = sd.findModel("clocks.AlarmClock").get().getScenarios().getByName("beep").get();
      IFeature feature = gh.findFeature(sdScenario).get();

      assertText(feature.getShortDescription(),
               service.with(feature).calling(f -> f.getShortDescription()).getLocation(),
               "AlarmClock.beep.feature");
      assertText(feature.getDescription(),
               service.with(feature).calling(f -> f.getDescription()).getLocation(), "AlarmClock.beep.feature");
      assertText("@tag", service.with(feature).calling(f -> f.getTags().iterator().next()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinScenario scenario = feature.getScenarios().iterator().next();
      assertText(scenario.getName(),
               service.with(scenario).calling(s -> s.getName()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinScenario scenarioOutline = new ArrayList<>(feature.getScenarios()).get(1);
      assertText(scenarioOutline.getName(),
               service.with(scenarioOutline).calling(s -> s.getName()).getLocation(),
               "AlarmClock.beep.feature");

      assertText("@tag1", service.with(scenario).calling(s -> s.getTags().iterator().next()).getLocation(),
               "AlarmClock.beep.feature");
      assertText("@tag2", service.with(scenarioOutline).calling(s -> s.getTags().iterator().next()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinScenario background = scenario.getBackground().get();
      assertText(background.getName(), service.with(background).calling(b -> b.getName()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinStep step = scenario.getGivens().get(0);
      IGherkinStep outlineStep = scenarioOutline.getThens().get(0);

      assertText(step.getContent(), service.with(step).calling(s -> s.getContent()).getLocation(),
               "AlarmClock.beep.feature");
      assertText("Given", service.with(step).calling(s -> s.getKeyword()).getLocation(), "AlarmClock.beep.feature");

      assertText(outlineStep.getContent(), service.with(outlineStep).calling(s -> s.getContent()).getLocation(),
               "AlarmClock.beep.feature");
      assertText("Then", service.with(outlineStep).calling(s -> s.getKeyword()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinTable table = step.getTableArgument().get();
      assertText("| 1       | true  |",
               service.with(table).calling(t -> t.getRows().get(1)).getLocation(),
               "AlarmClock.beep.feature");

      assertText("| example | table |\n      | 1       | true  |",
               service.with(table).calling(t -> t.getRows().subList(0, 2)).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinRow row = table.getRows().get(0);
      assertText("| example |",
               service.with(row).calling(r -> r.getCells().get(0)).getLocation(),
               "AlarmClock.beep.feature");
      IGherkinCell cell = row.getCells().get(0);
      assertText(cell.getValue(),
               service.with(cell).calling(c -> c.getValue()).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinTable examples = scenarioOutline.getExamples().get();
      assertText("| name  | value | status  |",
               service.with(examples).calling(e -> e.getHeader()).getLocation(),
               "AlarmClock.beep.feature");
      IGherkinRow header = examples.getHeader().get();
      assertText("| value | status  |",
               service.with(header).calling(h -> h.getCells().subList(1, 3)).getLocation(),
               "AlarmClock.beep.feature");
      IGherkinRow rowWithHeader = examples.getRows().get(0);
      assertText("| name1 |",
               service.with(rowWithHeader).calling(r -> r.getCell("name")).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinStep stepWithDocString = scenario.getSteps().get(1);
      assertText("\"\"\"\n      Example docstring\n      Line 2\n      \"\"\"",
               service.with(stepWithDocString).calling(s -> s.getDocStringArgument()).getLocation(),
               "AlarmClock.beep.feature");
   }

   /**
    * Asserts that the text from the source location is equal the expected text and that the source location's filename
    * is equal to the given file name.
    */
   private static void assertText(String expectedText, ISourceLocation location, String fileName) {
      try {
         assertEquals(fileName, location.getPath().getFileName().toString());
         String lines = Files.lines(location.getPath())
                  .skip(location.getLineNumber() - 1)
                  .collect(Collectors.joining("\n"));
         assertFalse("Column must be positive: " + location.getColumn(), location.getColumn() < 1);
         assertTrue("Column is too large: " + location.getColumn() + ", text: <" + lines + ">",
                  location.getColumn() < lines.length());
         assertFalse("Length cannot be negative: " + location.getLength(), location.getLength() < 0);
         assertTrue("Length extends text: " + lines.length() + ", column: " + location.getColumn() + ", text: <"
                  + lines + ">", location.getLength() + location.getColumn() <= lines.length());
         String actualText = lines.substring(location.getColumn() - 1, location.getColumn() - 1 + location.getLength());
         assertEquals(expectedText, actualText);
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

}
