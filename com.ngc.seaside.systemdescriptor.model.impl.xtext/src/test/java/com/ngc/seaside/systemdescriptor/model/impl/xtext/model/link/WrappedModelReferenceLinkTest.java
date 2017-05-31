package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class WrappedModelReferenceLinkTest extends AbstractWrappedXtextTest {

  private WrappedModelReferenceLink wrapped;

  private LinkDeclaration declaration;

  @Mock
  private IModel parent;

  @Mock
  private IModel sourceWrapper;

  @Mock
  private IModel targetWrapper;

  @Mock
  private IModelReferenceField wrappedSourceField;

  @Mock
  private IModelReferenceField wrappedTargetField;

  @Before
  public void setup() throws Throwable {
    Model sourceParent = factory().createModel();
    PartDeclaration source = factory().createPartDeclaration();
    source.setName("someSource");
    sourceParent.setParts(factory().createParts());
    sourceParent.getParts().getDeclarations().add(source);

    Model targetParent = factory().createModel();
    PartDeclaration target = factory().createPartDeclaration();
    target.setName("someTarget");
    targetParent.setParts(factory().createParts());
    targetParent.getParts().getDeclarations().add(target);

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

    INamedChildCollection<IModel, IModelReferenceField> sourceParts = new NamedChildCollection<>();
    INamedChildCollection<IModel, IModelReferenceField> targetParts = new NamedChildCollection<>();
    sourceParts.add(wrappedSourceField);
    targetParts.add(wrappedTargetField);
    when(sourceWrapper.getParts()).thenReturn(sourceParts);
    when(targetWrapper.getParts()).thenReturn(targetParts);

    when(resolver().getWrapperFor(sourceParent)).thenReturn(sourceWrapper);
    when(resolver().getWrapperFor(targetParent)).thenReturn(targetWrapper);
    when(resolver().getWrapperFor(xtextParent)).thenReturn(parent);
  }

  @Test
  public void testDoesWrapXTextObject() throws Throwable {
    wrapped = new WrappedModelReferenceLink(resolver(), declaration);
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
