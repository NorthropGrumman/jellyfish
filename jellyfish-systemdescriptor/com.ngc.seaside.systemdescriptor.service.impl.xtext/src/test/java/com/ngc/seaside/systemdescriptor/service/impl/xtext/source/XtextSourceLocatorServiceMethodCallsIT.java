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
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XtextSourceLocatorServiceMethodCallsIT {

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

      assertText("AlarmClock", service.with(model).then(IModel::getName).getLocation(), "AlarmClock.sd");

      assertText("clocks", service.with(model).then(IModel::getParent).getLocation(), "AlarmClock.sd");

      // Inputs
      assertText("many Time alarmTimes", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").getLocation(), "AlarmClock.sd");

      assertText("many Time alarmTimes", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").then(Optional::get).getLocation(),
               "AlarmClock.sd");

      assertText("alarmTimes", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").then(Optional::get)
               .then(IDataReferenceField::getName).getLocation(), "AlarmClock.sd");

      assertText("Time", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").then(Optional::get)
               .then(IDataReferenceField::getType).getLocation(), "AlarmClock.sd");

      assertText("Time", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").then(Optional::get)
               .then(IDataReferenceField::getType).then(IData::getName).getLocation(), "Time.sd");

      assertText("many", service.with(model).then(IModel::getInputs)
               .then(INamedChildCollection::getByName, "alarmTimes").then(Optional::get)
               .then(IDataReferenceField::getCardinality).getLocation(), "AlarmClock.sd");

      // Metadata
      assertText("\"The top level alarm clock system.  It requires the alarmTimes from some other component.\"",
               service.with(model).then(IModel::getMetadata).then(IMetadata::getJson)
                        .then(JsonObject::get, "description").getLocation(),
               "AlarmClock.sd");

      assertText("\"description\"", service.with(model).then(IModel::getMetadata).then(IMetadata::getJson)
               .then(JsonObject::containsKey, "description").getLocation(), "AlarmClock.sd");

      assertText("system",
               service.with(model).then(IModel::getMetadata).then(IMetadata::getJson)
                        .then(JsonObject::getJsonArray, "stereotypes").then(JsonArray::getString, 1).getLocation(),
               "AlarmClock.sd");

      // Parts
      assertText("timer", service.with(model).then(IModel::getParts)
               .then(INamedChildCollection::getByName, "timer").then(Optional::get).then(IModelReferenceField::getName)
               .getLocation(), "AlarmClock.sd");

      assertText("Timer", service.with(model).then(IModel::getParts)
               .then(INamedChildCollection::getByName, "timer").then(Optional::get)
               .then(IModelReferenceField::getType).getLocation(), "AlarmClock.sd");

      assertText("Timer", service.with(model).then(IModel::getParts)
               .then(INamedChildCollection::getByName, "timer").then(Optional::get)
               .then(IModelReferenceField::getType).then(IModel::getName).getLocation(), "Timer.sd");

      // Requires
      assertText("speaker", service.with(model).then(IModel::getRequiredModels)
               .then(INamedChildCollection::getByName, "speaker").then(Optional::get)
               .then(IModelReferenceField::getName)
               .getLocation(), "AlarmClock.sd");

      assertText("Speaker", service.with(model).then(IModel::getRequiredModels)
               .then(INamedChildCollection::getByName, "speaker").then(Optional::get)
               .then(IModelReferenceField::getType).getLocation(), "AlarmClock.sd");

      assertText("Speaker", service.with(model).then(IModel::getRequiredModels)
               .then(INamedChildCollection::getByName, "speaker").then(Optional::get)
               .then(IModelReferenceField::getType).then(IModel::getName).getLocation(), "Speaker.sd");

      // Scenario
      assertText("beep", service.with(model).then(IModel::getScenarios).then(INamedChildCollection::getByName, "beep")
               .then(Optional::get).then(IScenario::getName).getLocation(), "AlarmClock.sd");
   }

   @Test
   public void testDataLocations() {
      IData data = sd.findData("clocks.datatypes.ComplexTime").get();

      assertText("ComplexTime", service.with(data).then(IData::getName).getLocation(), "ComplexTime.sd");

      assertText("clocks.datatypes", service.with(data).then(IData::getParent).getLocation(), "ComplexTime.sd");

      assertText(
               "Represents a local time (does not account for timezones).", service.with(data).then(IData::getMetadata)
                        .then(IMetadata::getJson).then(JsonObject::getString, "description").getLocation(),
               "ComplexTime.sd");

      assertText("TimeZone timeZone", service.with(data).then(IData::getFields)
               .then(INamedChildCollection::getByName, "timeZone").getLocation(), "ComplexTime.sd");

      assertText("TimeZone timeZone",
               service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "timeZone")
                        .then(Optional::get).getLocation(),
               "ComplexTime.sd");

      assertText("TimeZone",
               service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "timeZone")
                        .then(Optional::get).then(IDataField::getReferencedEnumeration).getLocation(),
               "ComplexTime.sd");

      assertText("TimeZone",
               service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "timeZone")
                        .then(Optional::get).then(IDataField::getReferencedEnumeration).then(IEnumeration::getName)
                        .getLocation(),
               "TimeZone.sd");

      assertText("int", service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "hour")
               .then(Optional::get).then(IDataField::getType).getLocation(), "ComplexTime.sd");

      assertText("hour", service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "hour")
               .then(Optional::get).then(IDataField::getName).getLocation(), "ComplexTime.sd");

      assertText("0", service.with(data).then(IData::getFields).then(INamedChildCollection::getByName, "hour")
               .then(Optional::get).then(IDataField::getMetadata).then(IMetadata::getJson)
               .then(JsonObject::getJsonObject, "validation").then(JsonObject::get, "min").getLocation(),
               "ComplexTime.sd");
   }

   @Test
   public void testEnumLocations() {
      IEnumeration enumeration = sd.findEnumeration("clocks.datatypes.TimeZone").get();

      assertText("TimeZone", service.with(enumeration).then(IEnumeration::getName).getLocation(), "TimeZone.sd");

      assertText("clocks.datatypes", service.with(enumeration).then(IEnumeration::getParent).getLocation(),
               "TimeZone.sd");

      assertText("CST", service.with(enumeration).then(IEnumeration::getValues).then(Iterable::iterator)
               .then(Iterator::next).getLocation(), "TimeZone.sd");

      assertText("EST",
               service.with(enumeration).then(IEnumeration::getValues).then(Collection::contains, "EST").getLocation(),
               "TimeZone.sd");
   }

   @Test
   public void testScenarioLocations() {
      IScenario scenario = sd.findModel("clocks.AlarmClock").get().getScenarios().getByName("beep").get();

      assertText("beep", service.with(scenario).then(IScenario::getName).getLocation(), "AlarmClock.sd");

      assertText("value", service.with(scenario).then(IScenario::getMetadata).then(IMetadata::getJson)
               .then(JsonObject::getString, "example").getLocation(), "AlarmClock.sd");

      assertText("when TODO a b c", service.with(scenario).then(IScenario::getWhens).getLocation(), "AlarmClock.sd");

      assertText("TODO a b c", service.with(scenario).then(IScenario::getGivens).then(Iterable::iterator)
               .then(Iterator::next).getLocation(), "AlarmClock.sd");

      assertText("TODO", service.with(scenario).then(IScenario::getGivens).then(Iterable::iterator)
               .then(Iterator::next).then(IScenarioStep::getKeyword).getLocation(), "AlarmClock.sd");

      assertText("a b c", service.with(scenario).then(IScenario::getGivens).then(Iterable::iterator)
               .then(Iterator::next).then(IScenarioStep::getParameters).getLocation(), "AlarmClock.sd");

      assertText("a", service.with(scenario).then(IScenario::getGivens).then(Iterable::iterator)
               .then(Iterator::next).then(IScenarioStep::getParameters).then(List::get, 0).getLocation(),
               "AlarmClock.sd");

      assertText("b c", service.with(scenario).then(IScenario::getGivens).then(Iterable::iterator)
               .then(Iterator::next).then(IScenarioStep::getParameters).then(List::subList, 1, 3).getLocation(),
               "AlarmClock.sd");
   }

   @Test
   public void testGherkinLocations() {
      IScenario sdScenario = sd.findModel("clocks.AlarmClock").get().getScenarios().getByName("beep").get();
      IFeature feature = gh.findFeature(sdScenario).get();

      assertText(feature.getShortDescription(), service.with(feature).then(IFeature::getShortDescription).getLocation(),
               "AlarmClock.beep.feature");
      assertText(feature.getDescription(),
               service.with(feature).then(IFeature::getDescription).getLocation(), "AlarmClock.beep.feature");
      assertText("@tag", service.with(feature).then(IFeature::getTags).then(Iterable::iterator).then(Iterator::next)
               .getLocation(), "AlarmClock.beep.feature");

      IGherkinScenario scenario = feature.getScenarios().iterator().next();
      assertText(scenario.getName(),
               service.with(feature).then(IFeature::getScenarios).then(Iterable::iterator).then(Iterator::next)
                        .then(IGherkinScenario::getName).getLocation(),
               "AlarmClock.beep.feature");

      IGherkinScenario scenarioOutline = new ArrayList<>(feature.getScenarios()).get(1);
      assertText(scenarioOutline.getName(),
               service.with(feature).then(IFeature::getScenarios).then(ArrayList::new).then(List::get, 1)
                        .then(IGherkinScenario::getName).getLocation(),
               "AlarmClock.beep.feature");

      assertText("@tag1", service.with(scenario).then(IGherkinScenario::getTags).then(Iterable::iterator)
               .then(Iterator::next).getLocation(), "AlarmClock.beep.feature");
      assertText("@tag2", service.with(scenarioOutline).then(IGherkinScenario::getTags).then(Iterable::iterator)
               .then(Iterator::next).getLocation(), "AlarmClock.beep.feature");

      assertText(scenario.getBackground().get().getName(), service.with(scenario).then(IGherkinScenario::getBackground)
               .then(Optional::get).then(IGherkinScenario::getName).getLocation(), "AlarmClock.beep.feature");

      IGherkinStep step = scenario.getGivens().get(0);
      IGherkinStep outlineStep = scenarioOutline.getThens().get(0);

      assertText(step.getContent(), service.with(scenario).then(IGherkinScenario::getGivens).then(List::get, 0)
               .then(IGherkinStep::getContent).getLocation(), "AlarmClock.beep.feature");
      assertText("Given", service.with(step).then(IGherkinStep::getKeyword).getLocation(), "AlarmClock.beep.feature");

      assertText(outlineStep.getContent(), service.with(outlineStep).then(IGherkinStep::getContent).getLocation(),
               "AlarmClock.beep.feature");
      assertText("Then", service.with(outlineStep).then(IGherkinStep::getKeyword).getLocation(),
               "AlarmClock.beep.feature");

      assertText("| 1       | true  |",
               service.with(step).then(IGherkinStep::getTableArgument).then(Optional::get).then(IGherkinTable::getRows)
                        .then(List::get, 1).getLocation(),
               "AlarmClock.beep.feature");

      assertText("| example | table |\n      | 1       | true  |",
               service.with(step).then(IGherkinStep::getTableArgument).then(Optional::get).then(IGherkinTable::getRows)
                        .then(List::subList, 0, 2).getLocation(),
               "AlarmClock.beep.feature");

      assertText("| example |",
               service.with(step).then(IGherkinStep::getTableArgument).then(Optional::get).then(IGherkinTable::getRows)
                        .then(List::get, 0).then(IGherkinRow::getCells).then(List::get, 0).getLocation(),
               "AlarmClock.beep.feature");
      assertText(step.getTableArgument().get().getRows().get(0).getCells().get(0).getValue(),
               service.with(step).then(IGherkinStep::getTableArgument).then(Optional::get).then(IGherkinTable::getRows)
                        .then(List::get, 0).then(IGherkinRow::getCells).then(List::get, 0).then(IGherkinCell::getValue)
                        .getLocation(),
               "AlarmClock.beep.feature");

      assertText("| name  | value | status  |",
               service.with(scenarioOutline).then(IGherkinScenario::getExamples).then(Optional::get)
                        .then(IGherkinTable::getHeader).getLocation(),
               "AlarmClock.beep.feature");
      assertText("| name  | value | status  |",
               service.with(scenarioOutline).then(IGherkinScenario::getExamples).then(Optional::get)
                        .then(IGherkinTable::getHeader).then(Optional::get).getLocation(),
               "AlarmClock.beep.feature");
      assertText("| value | status  |",
               service.with(scenarioOutline).then(IGherkinScenario::getExamples).then(Optional::get)
                        .then(IGherkinTable::getHeader).then(Optional::get).then(IGherkinRow::getCells)
                        .then(List::subList, 1, 3).getLocation(),
               "AlarmClock.beep.feature");
      assertText("| name1 |",
               service.with(scenarioOutline).then(IGherkinScenario::getExamples).then(Optional::get)
                        .then(IGherkinTable::getRows).then(List::get, 0).then(IGherkinRow::getCell, "name")
                        .getLocation(),
               "AlarmClock.beep.feature");

      IGherkinStep stepWithDocString = scenario.getSteps().get(1);
      assertText("\"\"\"\n      Example docstring\n      Line 2\n      \"\"\"",
               service.with(stepWithDocString).then(IGherkinStep::getDocStringArgument).getLocation(),
               "AlarmClock.beep.feature");
      assertText("Example", service.with(stepWithDocString).then(IGherkinStep::getDocStringArgument).then(List::get, 0)
               .then(String::substring, 0, "Example".length()).getLocation(), "AlarmClock.beep.feature");
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
