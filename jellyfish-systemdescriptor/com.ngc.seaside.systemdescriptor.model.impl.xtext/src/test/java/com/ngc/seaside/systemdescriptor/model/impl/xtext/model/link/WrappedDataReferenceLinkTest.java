/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

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
// we are cheating and reusing the setup code for the model link test.
@RunWith(MockitoJUnitRunner.Silent.class)
public class WrappedDataReferenceLinkTest extends AbstractWrappedXtextTest {

   private WrappedDataReferenceLink wrapped;

   private BaseLinkDeclaration declaration;

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
      declaration = factory().createBaseLinkDeclaration();
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

   @Test
   public void testDoesTryToWrapXTextObject() throws Throwable {
      assertTrue("tryToWrap failed to wrap valid data declaration!",
                 WrappedDataReferenceLink.tryToWrap(resolver(), declaration).isPresent());

      LinkTestUtil.LinkTestSetup setup = WrappedModelReferenceLinkTest.getModelLinkDeclaration();
      assertFalse("tryToWrap should not wrap model declaration!",
                  WrappedDataReferenceLink.tryToWrap(setup.resolver, setup.declaration)
                        .isPresent());
   }

   /**
    * Gets a mocked link test setup.
    */
   public static LinkTestUtil.LinkTestSetup getDataLinkDeclaration() throws Throwable {
      WrappedDataReferenceLinkTest test = new WrappedDataReferenceLinkTest();
      MockitoAnnotations.initMocks(test);
      test.setup();
      return new LinkTestUtil.LinkTestSetup(test.declaration, test.resolver());
   }
}
