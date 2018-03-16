package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link.LinkTestUtil;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link.WrappedDataReferenceLinkTest;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link.WrappedModelReferenceLinkTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;

import java.util.Optional;

// Note we run this test with MockitoJUnitRunner.Silent to avoid UnnecessaryStubbingExceptions.  This happens because
// we are cheating and reusing the setup code for the data and model link tests.
@RunWith(MockitoJUnitRunner.Silent.class)
public class WrappedModelTest extends AbstractWrappedXtextTest {

   private WrappedModel wrapped;

   private Model model;

   @Mock
   private IPackage parent;

   @Before
   public void setup() throws Throwable {
      model = factory().createModel();
      model.setName("MyModel");

      Data data = factory().createData();
      data.setName("Foo");

      Model modelType = factory().createModel();
      modelType.setName("Bar");

      InputDeclaration input = factory().createInputDeclaration();
      input.setName("input");
      input.setType(data);
      model.setInput(factory().createInput());
      model.getInput().getDeclarations().add(input);

      OutputDeclaration output = factory().createOutputDeclaration();
      output.setName("output");
      output.setType(data);
      model.setOutput(factory().createOutput());
      model.getOutput().getDeclarations().add(output);

      BaseRequireDeclaration require = factory().createBaseRequireDeclaration();
      require.setName("require");
      require.setType(modelType);
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(require);

      BasePartDeclaration part = factory().createBasePartDeclaration();
      part.setName("part");
      part.setType(modelType);
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(part);

      Scenario scenario = factory().createScenario();
      scenario.setName("myScenario");
      model.getScenarios().add(scenario);

      PrimitivePropertyFieldDeclaration primitiveProperty = factory().createPrimitivePropertyFieldDeclaration();
      primitiveProperty.setType(PrimitiveDataType.INT);
      primitiveProperty.setCardinality(Cardinality.DEFAULT);
      primitiveProperty.setName("x");

      Properties properties = factory().createProperties();
      properties.getDeclarations().add(primitiveProperty);
      model.setProperties(properties);

      Package p = factory().createPackage();
      p.setName("my.package");
      p.setElement(model);
      when(resolver().getWrapperFor(p)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedModel(resolver(), model);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   wrapped.getName());
      assertEquals("fully qualified name not correct!",
                   "my.package.MyModel",
                   wrapped.getFullyQualifiedName());
      assertEquals("parent not correct!",
                   parent,
                   wrapped.getParent());
      assertEquals("metadata not set!",
                   IMetadata.EMPTY_METADATA,
                   wrapped.getMetadata());

      String inputName = model.getInput().getDeclarations().get(0).getName();
      assertEquals("did not get input!",
                   inputName,
                   wrapped.getInputs().getByName(inputName).get().getName());

      String outputName = model.getOutput().getDeclarations().get(0).getName();
      assertEquals("did not get output!",
                   outputName,
                   wrapped.getOutputs().getByName(outputName).get().getName());

      String requireName = model.getRequires().getDeclarations().get(0).getName();
      assertEquals("did not get requirements!",
                   requireName,
                   wrapped.getRequiredModels().getByName(requireName).get().getName());

      String partName = model.getParts().getDeclarations().get(0).getName();
      assertEquals("did not get parts!",
                   partName,
                   wrapped.getParts().getByName(partName).get().getName());

      String scenarioName = model.getScenarios().get(0).getName();
      assertEquals("did not get scenario!",
                   scenarioName,
                   wrapped.getScenarios().getByName(scenarioName).get().getName());

      String propertyName = model.getProperties().getDeclarations().get(0).getName();
      Optional<IProperty> property = wrapped.getProperties().getByName(propertyName);
      assertTrue("property not present!",
                 property.isPresent());
      assertEquals("property type not correct!",
                   DataTypes.INT,
                   property.get().getType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   property.get().getCardinality());
   }

   @Test
   public void testDoesWrapModelWithDataLinks() throws Throwable {
      LinkTestUtil.LinkTestSetup setup = WrappedDataReferenceLinkTest.getDataLinkDeclaration();
      model.setLinks(factory().createLinks());
      model.getLinks().getDeclarations().add(setup.declaration);

      wrapped = new WrappedModel(resolver(), model);
      assertEquals("did not wrap links!",
                   1,
                   wrapped.getLinks().size());
   }

   @Test
   public void testDoesWrapModelWithModelLinks() throws Throwable {
      LinkTestUtil.LinkTestSetup setup = WrappedModelReferenceLinkTest.getModelLinkDeclaration();
      model.setLinks(factory().createLinks());
      model.getLinks().getDeclarations().add(setup.declaration);

      wrapped = new WrappedModel(resolver(), model);
      assertEquals("did not wrap links!",
                   1,
                   wrapped.getLinks().size());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrapped = new WrappedModel(resolver(), model);

      wrapped.getInputs().clear();
      assertTrue("did not update inputs!",
                 model.getInput().getDeclarations().isEmpty());

      wrapped.getOutputs().clear();
      assertTrue("did not update outputs!",
                 model.getOutput().getDeclarations().isEmpty());

      wrapped.getRequiredModels().clear();
      assertTrue("did not update requirements!",
                 model.getRequires().getDeclarations().isEmpty());

      wrapped.getParts().clear();
      assertTrue("did not update parts!",
                 model.getParts().getDeclarations().isEmpty());

      wrapped.getScenarios().clear();
      assertTrue("did not update scenarios!",
                 model.getScenarios().isEmpty());

      wrapped.setMetadata(newMetadata("foo", "bar"));
      assertNotNull("metadata not set!",
                    model.getMetadata());
   }
}
