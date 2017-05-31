package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.io.Closeables;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.ModelFieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WrappedSystemDescriptorIT {

   private final Map<String, InputStream> streams = new HashMap<>();

   private WrappedSystemDescriptor wrapped;

   @Inject
   private IParser parser;

   @Inject
   private XtextResourceSet resourceSet;

   @Before
   public void setup() throws Throwable {
      Injector injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
      injector.injectMembers(this);
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }


   @Test
   public void doesCreateWrappedDescriptorWithSingleResource() throws Throwable {
      IParseResult result = parser.parse(new InputStreamReader(streamOf("clocks/datatypes/Time.sd")));
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
      resourceOf("clocks/datatypes/Time.sd");
      Resource timerResource = resourceOf("clocks/models/Timer.sd");
      resourceOf("clocks/models/ClockDisplay.sd");
      resourceOf("clocks/models/Speaker.sd");
      resourceOf("clocks/models/Alarm.sd");
      resourceOf("clocks/AlarmClock.sd");

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
                   ModelFieldCardinality.SINGLE,
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
      step = s.getThens().iterator().next();
      assertEquals("then step not correct!",
                   "publish",
                   step.getKeyword());
      assertEquals("then step not correct!",
                   Collections.singletonList("currentTime"),
                   step.getParameters());

      model = wrapped.findModel("clocks.models", "ClockDisplay");
      assertTrue("model not found!",
                 model.isPresent());
      dataRefField = model.get().getInputs().getByName("currentTime");
      assertTrue("input field not set!",
                 dataRefField.isPresent());
      df = dataRefField.get();
      assertEquals("cardinality not correct!",
                   ModelFieldCardinality.SINGLE,
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

   @After
   public void teardown() throws Throwable {
      streams.values().forEach(Closeables::closeQuietly);
   }

   private InputStream streamOf(String resource) throws Throwable {
      InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
      streams.put(resource, is);
      return is;
   }

   private Resource resourceOf(String resource) throws Throwable {
      Resource r = resourceSet.createResource(URI.createURI("dummy:/" + resource));
      r.load(streamOf(resource), resourceSet.getLoadOptions());
      return r;
   }
}
