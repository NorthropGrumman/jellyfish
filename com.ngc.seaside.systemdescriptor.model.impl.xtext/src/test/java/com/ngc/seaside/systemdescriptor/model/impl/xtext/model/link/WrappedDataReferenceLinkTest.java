package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class WrappedDataReferenceLinkTest extends AbstractWrappedXtextTest {

  private WrappedDataReferenceLink wrapped;

  private LinkDeclaration declaration;

  @Mock
  private IModel parent;

  @Mock
  private IModel sourceWrapper;

  @Mock
  private IModel targetWrapper;

  @Mock
  private IDataReferenceField wrappedSourceField;

  @Mock
  private IDataReferenceField wrappedTargetField;

  @Before
  public void setup() throws Throwable {
    Model sourceParent = factory().createModel();
    InputDeclaration source = factory().createInputDeclaration();
    source.setName("someSource");
    sourceParent.setInput(factory().createInput());
    sourceParent.getInput().getDeclarations().add(source);

    Model targetParent = factory().createModel();
    InputDeclaration target = factory().createInputDeclaration();
    target.setName("someTarget");
    targetParent.setInput(factory().createInput());
    targetParent.getInput().getDeclarations().add(target);

    FieldReference sourceRef = factory().createFieldReference();
    sourceRef.setFieldDeclaration(source);

    LinkableExpression targetExpression = factory().createLinkableExpression();
    targetExpression.setTail(target);

    Model xtextParent = factory().createModel();
    declaration = factory().createLinkDeclaration();
    declaration.setSource(sourceRef);
    declaration.setTarget(targetExpression);
    xtextParent.setLinks(factory().createLinks());
    xtextParent.getLinks().getDeclarations().add(declaration);

    when(wrappedSourceField.getName()).thenReturn(source.getName());
    when(wrappedTargetField.getName()).thenReturn(target.getName());

    INamedChildCollection<IModel, IDataReferenceField> sourceInputs = new NamedChildCollection<>();
    INamedChildCollection<IModel, IDataReferenceField> targetInputs = new NamedChildCollection<>();
    sourceInputs.add(wrappedSourceField);
    targetInputs.add(wrappedTargetField);
    when(sourceWrapper.getInputs()).thenReturn(sourceInputs);
    when(targetWrapper.getInputs()).thenReturn(targetInputs);

    when(resolver().getWrapperFor(sourceParent)).thenReturn(sourceWrapper);
    when(resolver().getWrapperFor(targetParent)).thenReturn(targetWrapper);
    when(resolver().getWrapperFor(xtextParent)).thenReturn(parent);
  }

  @Test
  public void testDoesWrapXTextObject() throws Throwable {
    wrapped = new WrappedDataReferenceLink(resolver(), declaration);
    assertEquals("parent not correct!",
                 parent,
                 wrapped.getParent());
    assertEquals("source not correct!",
                 wrappedSourceField,
                 wrapped.getSource());
    assertEquals("target not correct!",
                 wrappedTargetField,
                 wrapped.getTarget());
  }
}
