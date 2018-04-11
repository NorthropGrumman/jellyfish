package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.io.Closeables;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class WrappedSystemDescriptorIT {

   private final Map<String, InputStream> streams = new HashMap<>();

   private WrappedSystemDescriptor wrapped;

   @Inject
   private IParser parser;

   private XtextResourceSet resourceSet;

   @Before
   public void setup() throws Throwable {
      TerminalsStandaloneSetup.doSetup();
      Injector injector = Guice.createInjector(new SystemDescriptorRuntimeModule());
      new SystemDescriptorStandaloneSetup().register(injector);
      injector.injectMembers(this);

      resourceSet = new XtextResourceSet();
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }

   @Test
   public void doesCreateWrappedDescriptorWithSingleResource() throws Throwable {
      IParseResult result = parser.parse(new InputStreamReader(streamOf(pathTo("clocks/datatypes/Time.sd"))));
      Package p = (Package) result.getRootASTElement();
      wrapped = new WrappedSystemDescriptor(p);

      Optional<IData> data = wrapped.findData("clocks.datatypes", "Time");
      assertTrue("data not found!",
                 data.isPresent());

      JsonObject json = data.get().getMetadata().getJson();
      assertNotNull("metadata not set!",
                    json);
      assertEquals("metadata not correct!",
                   "Time",
                   json.getString("name"));

      Optional<IDataField> field = data.get().getFields().getByName("hour");
      assertTrue("data field not set!",
                 field.isPresent());
      assertTrue("data field not set!",
                 data.get().getFields().getByName("minute").isPresent());
      assertTrue("data field not set!",
                 data.get().getFields().getByName("second").isPresent());

      IDataField f = field.get();
      json = f.getMetadata().getJson();
      assertNotNull("metadata not set on field!",
                    json);
      assertEquals("metadata not correct!",
                   0,
                   json.getJsonObject("validation").getJsonNumber("min").intValue());
      assertEquals("metadata not correct!",
                   23,
                   json.getJsonObject("validation").getJsonNumber("max").intValue());
      assertEquals("parent not correct!",
                   data.get(),
                   f.getParent());
   }

   @SuppressWarnings("unchecked")
   @Test
   public void testDoesCreateWrappedDescriptorWithMultipleResources() throws Throwable {
      resourceOf(pathTo("clocks/datatypes/Time.sd"));
      Resource timerResource = resourceOf(pathTo("clocks/models/Timer.sd"));
      resourceOf(pathTo("clocks/models/ClockDisplay.sd"));
      resourceOf(pathTo("clocks/models/Speaker.sd"));
      resourceOf(pathTo("clocks/models/Alarm.sd"));
      resourceOf(pathTo("clocks/AlarmClock.sd"));
      assertValid();

      // This is how you get the parsing result from an XText resource.  The result has the errors.
      IParseResult result = ((XtextResource) timerResource).getParseResult();
      assertFalse("should not have errors!",
                  result.hasSyntaxErrors());
      // You can also get validation errors with this:
      IResourceValidator validator = ((XtextResource) timerResource)
            .getResourceServiceProvider()
            .getResourceValidator();
      List<Issue> issues = validator.validate(timerResource, CheckMode.ALL, CancelIndicator.NullImpl);
      assertTrue("should not have issues!",
                 issues.isEmpty());

      Package p = (Package) timerResource.getContents().get(0);
      wrapped = new WrappedSystemDescriptor(p);

      // Check the data.
      Optional<IData> data = wrapped.findData("clocks.datatypes", "Time");
      assertTrue("data not found!",
                 data.isPresent());

      JsonObject json = data.get().getMetadata().getJson();
      assertNotNull("metadata not set!",
                    json);
      assertEquals("metadata not correct!",
                   "Time",
                   json.getString("name"));

      Optional<IDataField> field = data.get().getFields().getByName("hour");
      assertTrue("data field not set!",
                 field.isPresent());
      assertTrue("data field not set!",
                 data.get().getFields().getByName("minute").isPresent());
      assertTrue("data field not set!",
                 data.get().getFields().getByName("second").isPresent());

      IDataField f = field.get();
      json = f.getMetadata().getJson();
      assertNotNull("metadata not set on field!",
                    json);
      assertEquals("metadata not correct!",
                   0,
                   json.getJsonObject("validation").getJsonNumber("min").intValue());
      assertEquals("metadata not correct!",
                   23,
                   json.getJsonObject("validation").getJsonNumber("max").intValue());
      assertEquals("parent not correct!",
                   data.get(),
                   f.getParent());

      // Check the model.
      Optional<IModel> model = wrapped.findModel("clocks.models", "Timer");
      assertTrue("model not found!",
                 model.isPresent());

      json = model.get().getMetadata().getJson();
      assertNotNull("metadata not set!",
                    json);
      assertEquals("metadata not correct!",
                   "Timer",
                   json.getString("name"));

      Optional<IDataReferenceField> dataRefField = model.get().getOutputs().getByName("currentTime");
      assertTrue("output field not set!",
                 dataRefField.isPresent());
      IDataReferenceField df = dataRefField.get();
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   df.getCardinality());
      assertEquals("parent not correct!",
                   model.get(),
                   df.getParent());
      assertEquals("type not correct!",
                   data.get(),
                   df.getType());

      Optional<IScenario> scenario = model.get().getScenarios().getByName("tick");
      assertTrue("scenario not correct!",
                 scenario.isPresent());
      IScenario s = scenario.get();
      assertTrue("givens not correct!",
                 s.getGivens().isEmpty());
      assertEquals("whens not correct!",
                   1,
                   s.getWhens().size());
      assertEquals("thens not correct",
                   1,
                   s.getThens().size());
      IScenarioStep step = s.getWhens().iterator().next();
      assertEquals("when step not correct!",
                   "periodOf",
                   step.getKeyword());
      assertEquals("when step not correct!",
                   Arrays.asList("1", "sec", "elapses"),
                   step.getParameters());
      assertEquals("step parent not correct!",
                   s,
                   step.getParent());
      step = s.getThens().iterator().next();
      assertEquals("then step not correct!",
                   "publish",
                   step.getKeyword());
      assertEquals("then step not correct!",
                   Collections.singletonList("currentTime"),
                   step.getParameters());
      assertEquals("step parent not correct!",
                   s,
                   step.getParent());

      model = wrapped.findModel("clocks.models", "ClockDisplay");
      assertTrue("model not found!",
                 model.isPresent());
      dataRefField = model.get().getInputs().getByName("currentTime");
      assertTrue("input field not set!",
                 dataRefField.isPresent());
      df = dataRefField.get();
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   df.getCardinality());
      assertEquals("parent not correct!",
                   model.get(),
                   df.getParent());
      assertEquals("type not correct!",
                   data.get(),
                   df.getType());

      model = wrapped.findModel("clocks.models", "Alarm");
      assertTrue("model not found!",
                 model.isPresent());
      Optional<IModelReferenceField> modelRefField = model.get().getRequiredModels().getByName("speaker");
      assertTrue("requirement not set!",
                 modelRefField.isPresent());
      IModelReferenceField mf = modelRefField.get();
      assertEquals("parent not correct!",
                   model.get(),
                   mf.getParent());
      assertEquals("type not correct!",
                   wrapped.findModel("clocks.models", "Speaker").get(),
                   mf.getType());

      model = wrapped.findModel("clocks", "AlarmClock");
      assertTrue("model not found!",
                 model.isPresent());
      modelRefField = model.get().getParts().getByName("speaker");
      assertTrue("part not set!",
                 modelRefField.isPresent());
      mf = modelRefField.get();
      assertEquals("parent not correct!",
                   model.get(),
                   mf.getParent());
      assertEquals("type not correct!",
                   wrapped.findModel("clocks.models", "Speaker").get(),
                   mf.getType());

      Collection<IModelLink<?>> links = model.get().getLinks();
      assertEquals("missing links!",
                   4,
                   links.size());

      Iterator<IModelLink<?>> i = links.iterator();
      IModelLink<IDataReferenceField> dataLink = (IModelLink<IDataReferenceField>) i.next();
      assertEquals("parent not correct!",
                   model.get(),
                   dataLink.getParent());
      assertEquals("link source not correct!",
                   wrapped.findModel("clocks.models", "Timer").get().getOutputs().getByName("currentTime").get(),
                   dataLink.getSource());
      assertEquals("link target not correct!",
                   wrapped.findModel("clocks.models", "ClockDisplay").get().getInputs().getByName("currentTime").get(),
                   dataLink.getTarget());

      dataLink = (IModelLink<IDataReferenceField>) i.next();
      assertEquals("parent not correct!",
                   model.get(),
                   dataLink.getParent());
      assertEquals("link source not correct!",
                   wrapped.findModel("clocks.models", "Timer").get().getOutputs().getByName("currentTime").get(),
                   dataLink.getSource());
      assertEquals("link target not correct!",
                   wrapped.findModel("clocks.models", "Alarm").get().getInputs().getByName("currentTime").get(),
                   dataLink.getTarget());

      IModelLink<IModelReferenceField> modelLink = (IModelLink<IModelReferenceField>) i.next();
      assertEquals("parent not correct!",
                   model.get(),
                   modelLink.getParent());
      assertEquals("link source not correct!",
                   model.get().getParts().getByName("speaker").get(),
                   modelLink.getSource());
      assertEquals("link target not correct!",
                   wrapped.findModel("clocks.models", "Alarm").get().getRequiredModels().getByName("speaker").get(),
                   modelLink.getTarget());

      dataLink = (IModelLink<IDataReferenceField>) i.next();
      assertEquals("parent not correct!",
                   model.get(),
                   dataLink.getParent());
      assertEquals("link source not correct!",
                   model.get().getInputs().getByName("alarmTimes").get(),
                   dataLink.getSource());
      assertEquals("link target not correct!",
                   wrapped.findModel("clocks.models", "Alarm").get().getInputs().getByName("alarmTimes").get(),
                   dataLink.getTarget());
   }

   @Test
   public void testDoesCreateWrappedDescriptorWithDataInheritance() throws Throwable {
      resourceOf(pathTo("clocks/datatypes/Time.sd"));
      resourceOf(pathTo("clocks/datatypes/TimeZone.sd"));
      Resource goTimeResource = resourceOf(pathTo("clocks/datatypes/GoTime.sd"));
      resourceOf(pathTo("clocks/models/Timer.sd"));
      resourceOf(pathTo("clocks/models/ClockDisplay.sd"));
      resourceOf(pathTo("clocks/models/Speaker.sd"));
      resourceOf(pathTo("clocks/models/Alarm.sd"));
      resourceOf(pathTo("clocks/AlarmClock.sd"));

      // This is how you get the parsing result from an XText resource.  The result has the errors.
      IParseResult result = ((XtextResource) goTimeResource).getParseResult();
      assertFalse("should not have errors!",
                  result.hasSyntaxErrors());
      assertValid();

      Package p = (Package) goTimeResource.getContents().get(0);
      wrapped = new WrappedSystemDescriptor(p);

      Optional<IData> data = wrapped.findData("clocks.datatypes", "GoTime");
      assertTrue("did not find data!",
                 data.isPresent());

      Optional<IData> superType = data.get().getExtendedDataType();
      assertTrue("data super type not set!",
                 superType.isPresent());
      assertEquals("superType fully qualified name is not correct!",
                   "clocks.datatypes.Time",
                   superType.get().getFullyQualifiedName());
   }

   @Test
   public void testDoesCreateWrappedDescriptorWithEnums() throws Throwable {
      resourceOf(pathTo("clocks/datatypes/Time.sd"));
      resourceOf(pathTo("clocks/datatypes/TimeZone.sd"));
      Resource goTimeResource = resourceOf(pathTo("clocks/datatypes/GoTime.sd"));
      resourceOf(pathTo("clocks/models/Timer.sd"));
      resourceOf(pathTo("clocks/models/ClockDisplay.sd"));
      resourceOf(pathTo("clocks/models/Speaker.sd"));
      resourceOf(pathTo("clocks/models/Alarm.sd"));
      resourceOf(pathTo("clocks/AlarmClock.sd"));

      IParseResult result = ((XtextResource) goTimeResource).getParseResult();
      assertFalse("should not have errors!",
                  result.hasSyntaxErrors());
      assertValid();

      Package p = (Package) goTimeResource.getContents().get(0);
      wrapped = new WrappedSystemDescriptor(p);

      Optional<IEnumeration> timeZone = wrapped.findEnumeration("clocks.datatypes", "TimeZone");
      assertTrue("did not find enum!",
                 timeZone.isPresent());

      assertTrue("missing enum value!",
                 timeZone.get().getValues().contains("CST"));
      assertTrue("missing enum value!",
                 timeZone.get().getValues().contains("EST"));
      assertTrue("missing enum value!",
                 timeZone.get().getValues().contains("MST"));
      assertTrue("missing enum value!",
                 timeZone.get().getValues().contains("PST"));

   }

   @Test
   public void testDoesCreateWrappedDescriptorWithRefinedModelsAndProperties() throws Throwable {
      resourceOf(pathTo("clocks/datatypes/Time.sd"));
      resourceOf(pathTo("clocks/datatypes/TimeZone.sd"));
      resourceOf(pathTo("clocks/datatypes/ConfigData.sd"));
      resourceOf(pathTo("clocks/models/Timer.sd"));
      resourceOf(pathTo("clocks/models/ClockDisplay.sd"));
      resourceOf(pathTo("clocks/models/Speaker.sd"));
      resourceOf(pathTo("clocks/models/Alarm.sd"));
      resourceOf(pathTo("clocks/AlarmClock.sd"));
      Resource refinedResource = resourceOf(pathTo("clocks/LoudAlarmClock.sd"));

      IParseResult result = ((XtextResource) refinedResource).getParseResult();
      assertFalse("should not have errors!",
                  result.hasSyntaxErrors());
      assertValid();

      Package p = (Package) refinedResource.getContents().get(0);
      wrapped = new WrappedSystemDescriptor(p);

      Optional<IModel> loudClock = wrapped.findModel("clocks", "LoudAlarmClock");
      assertTrue("did not find refined model!",
                 loudClock.isPresent());
      assertTrue("refined model not set!",
                 loudClock.get().getRefinedModel().isPresent());
      assertEquals("refined model not correct!",
                   wrapped.findModel("clocks", "AlarmClock").get(),
                   loudClock.get().getRefinedModel().get());
      assertTrue("refined field not correct!",
                 loudClock.get().getParts().getByName("speaker").get().getRefinedField().isPresent());
      assertTrue("refined link not correct!",
                 loudClock.get().getLinkByName("speakerConnection").get().getRefinedLink().isPresent());
      assertTrue("refined link not correct!",
                 loudClock.get().getLinks().iterator().next().getRefinedLink().isPresent());

      IProperties properties = loudClock.get().getProperties();
      assertEquals("property value not correct!",
                   BigInteger.ONE,
                   properties.resolveAsInteger("a").orElse(null));
      assertEquals("property value not correct!",
                   "hello",
                   properties.resolveAsString("b").orElse(null));
      assertEquals("property value not correct!",
                   true,
                   properties.resolveAsBoolean("c").orElse(null));
      assertEquals("property value not correct!",
                   "CST",
                   properties.resolveAsEnumeration("zone").get().getValue());

      assertEquals("property value not correct!",
                   new BigInteger("2"),
                   properties.resolveAsInteger("config", "x").orElse(null));
      assertEquals("property value not correct!",
                   "world",
                   properties.resolveAsString("config", "y").orElse(null));
      assertEquals("property value not correct!",
                   false,
                   properties.resolveAsBoolean("config", "z").orElse(null));
      assertEquals("property value not correct!",
                   "CST",
                   properties.resolveAsEnumeration("config", "timeZone").get().getValue());

      assertFalse("missing property should not be present!",
                  properties.resolveAsData("foo").isPresent());
   }

   @After
   public void teardown() {
      streams.values().forEach(Closeables::closeQuietly);
   }

   private void assertValid() {
      Iterator<Resource> i = resourceSet.getResources().iterator();
      XtextResource resource = (XtextResource) i.next();
      // Get the validator.
      IResourceValidator validator = resource.getResourceServiceProvider().getResourceValidator();
      do {
         List<Issue> issues = validator.validate(resource, CheckMode.ALL, null);
         issues = issues.stream()
               .filter(issue -> issue.getSeverity() == Severity.ERROR)
               .collect(Collectors.toList());
         if (!issues.isEmpty()) {
            StringBuilder sb = new StringBuilder("files failed validation!  ");
            issues.forEach(issue -> sb.append(issue.getMessage()));
            fail(sb.toString());
         }
         resource = i.hasNext() ? (XtextResource) i.next() : null;
      } while (resource != null);
   }

   private InputStream streamOf(Path file) throws IOException {
      InputStream is = Files.newInputStream(file);
      streams.put(file.toString(), is);
      return is;
   }

   private XtextResource resourceOf(Path file) throws IOException {
      XtextResource r = (XtextResource) resourceSet.createResource(
            URI.createFileURI(file.toAbsolutePath().toFile().toString()));
      r.load(streamOf(file), resourceSet.getLoadOptions());
      return r;
   }

   /**
    * Creates a path to the given packages and files.
    */
   public static Path pathTo(String... packagesAndFile) {
      Collection<String> parts = new ArrayList<>();
      parts.add("resources");
      parts.add("test");
      parts.addAll(Arrays.asList(packagesAndFile));
      return Paths.get("build", parts.toArray(new String[parts.size()]));
   }
}
