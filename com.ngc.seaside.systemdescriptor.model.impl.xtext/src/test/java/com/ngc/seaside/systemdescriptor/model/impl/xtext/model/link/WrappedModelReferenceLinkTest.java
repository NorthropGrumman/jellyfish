package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

// Note we run this test with MockitoJUnitRunner.Silent to avoid UnnecessaryStubbingExceptions.  This happens because
// we are cheating and reusing the setup code for the data link test.
@RunWith(MockitoJUnitRunner.Silent.class)
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

  @Test
  public void testDoesTryToWrapXTextObject() throws Throwable {
    assertTrue("tryToWrap failed to wrap valid model declaration!",
               WrappedModelReferenceLink.tryToWrap(resolver(), declaration).isPresent());

    LinkTestUtil.LinkTestSetup setup = WrappedDataReferenceLinkTest.getDataLinkDeclaration();
    assertFalse("tryToWrap should not wrap model declaration!",
                WrappedModelReferenceLink.tryToWrap(setup.resolver, setup.declaration)
                    .isPresent());
  }

  public static LinkTestUtil.LinkTestSetup getModelLinkDeclaration() throws Throwable {
    WrappedModelReferenceLinkTest test = new WrappedModelReferenceLinkTest();
    MockitoAnnotations.initMocks(test);
    test.setup();
    return new LinkTestUtil.LinkTestSetup(test.declaration, test.resolver());
  }
}
