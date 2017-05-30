package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

    wrapped.setMetadata(newMetadata("foo", "bar"));
    assertNotNull("metadata not set!",
                  model.getMetadata());
  }
}
