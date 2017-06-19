package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

// Note we run this test with MockitoJUnitRunner.Silent to avoid UnnecessaryStubbingExceptions.  This happens because
// we are cheating and reusing the setup code for the data and model link tests.
@RunWith(MockitoJUnitRunner.Silent.class)
public class WrappedModelMetadataTest extends AbstractWrappedXtextTest
{

   private WrappedModel wrapped;

   private Model model;

   @Mock
   private IPackage parent;

   @Before
   public void setup() throws Throwable
   {
      model = factory().createModel();
      model.setName("AlarmClock");

      Data time = factory().createData();
      time.setName("Time");

      Model timer = factory().createModel();
      timer.setName("Timer");

      Model gatorade = factory().createModel();
      timer.setName("Gatorade");

      InputDeclaration input = factory().createInputDeclaration();
      input.setName("alarmTimes");
      input.setCardinality(Cardinality.MANY);
      input.setType(time);
      input.setMetadata(getMetatdata("satisfies", "requirement1"));
      model.setInput(factory().createInput());
      model.getInput().getDeclarations().add(input);

      OutputDeclaration output = factory().createOutputDeclaration();
      output.setName("timeOfAlarmDeactivation");
      output.setType(time);
      output.setMetadata(getMetatdata("since", "2.0"));
      model.setOutput(factory().createOutput());
      model.getOutput().getDeclarations().add(output);

      PartDeclaration part = factory().createPartDeclaration();
      part.setName("timer");
      part.setType(timer);
      part.setMetadata(getMetatdata("description", "This is provided by 3rd party software"));
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(part);

      RequireDeclaration require = factory().createRequireDeclaration();
      require.setName("thristQuencher");
      require.setType(gatorade);
      require.setMetadata(getMetatdata("flavor", "purple"));
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(require);

      Package p = factory().createPackage();
      p.setName("clock.models");
      p.setElement(model);
      when(resolver().getWrapperFor(p)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable
   {
      wrapped = new WrappedModel(resolver(), model);
      assertEquals("name not correct!", "AlarmClock", wrapped.getName());
      assertEquals("fully qualified name not correct!", "clock.models.AlarmClock", wrapped.getFullyQualifiedName());
      assertEquals("parent not correct!", parent, wrapped.getParent());
      assertEquals("metadata not set!", IMetadata.EMPTY_METADATA, wrapped.getMetadata());

      String inputName = "alarmTimes";
      assertEquals("did not get input!", inputName, wrapped.getInputs().getByName(inputName).get().getName());
      assertEquals("requirement1", wrapped.getInputs().getByName(inputName).get().getMetadata().getJson().getString("satisfies"));

      String outputName = "timeOfAlarmDeactivation";
      assertEquals("did not get output!", outputName, wrapped.getOutputs().getByName(outputName).get().getName());
      assertEquals("2.0", wrapped.getOutputs().getByName(outputName).get().getMetadata().getJson().getString("since"));

      String partName = "timer";
      assertEquals("did not get parts!", partName, wrapped.getParts().getByName(partName).get().getName());
      assertEquals("This is provided by 3rd party software", wrapped.getParts().getByName(partName).get().getMetadata().getJson().getString("description"));

      String requireName = "thristQuencher";
      assertEquals("did not get requirements!", requireName, wrapped.getRequiredModels().getByName(requireName).get().getName());
      assertEquals("purple", wrapped.getRequiredModels().getByName(requireName).get().getMetadata().getJson().getString("flavor"));

   }

   private static JsonObject getMetatdata(String key, String value)
   {
      StringValue stringValue = SystemDescriptorFactory.eINSTANCE.createStringValue();
      stringValue.setValue(value);
      Member m = SystemDescriptorFactory.eINSTANCE.createMember();
      m.setKey(key);
      m.setValue(stringValue);
      JsonObject obj = SystemDescriptorFactory.eINSTANCE.createJsonObject();
      obj.getMembers().add(m);
      return obj;
   }
}
