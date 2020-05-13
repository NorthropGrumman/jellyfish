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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedBaseRequireModelReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedBaseRequireModelReferenceField wrapped;

   private BaseRequireDeclaration baseRequireDeclaration;

   private Model modelType;

   @Mock
   private IModel parent;

   @Mock
   private IModel wrappedModelType;

   @Before
   public void setup() throws Throwable {
      baseRequireDeclaration = factory().createBaseRequireDeclaration();
      baseRequireDeclaration.setName("require1");

      modelType = factory().createModel();
      modelType.setName("MyType");
      baseRequireDeclaration.setType(modelType);

      Model model = factory().createModel();
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(baseRequireDeclaration);

      IPackage p = mock(IPackage.class);
      when(p.getName()).thenReturn("some.package");
      when(wrappedModelType.getParent()).thenReturn(p);

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(modelType)).thenReturn(wrappedModelType);
      when(resolver().findXTextModel(model.getName(), p.getName())).thenReturn(Optional.of(modelType));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedBaseRequireModelReferenceField(resolver(), baseRequireDeclaration);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   baseRequireDeclaration.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrapped.getParent());
      assertEquals("type not correct!",
                   wrappedModelType,
                   wrapped.getType());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      Model newXtextType = factory().createModel();
      IModel newType = mock(IModel.class);
      IPackage p = mock(IPackage.class);
      when(newType.getName()).thenReturn("NewModel");
      when(newType.getParent()).thenReturn(p);
      when(p.getName()).thenReturn("some.package");
      when(resolver().findXTextModel(newType.getName(), p.getName())).thenReturn(Optional.of(newXtextType));

      wrapped = new WrappedBaseRequireModelReferenceField(resolver(), baseRequireDeclaration);
      wrapped.setType(newType);
      assertEquals("type not updated!",
                   newXtextType,
                   baseRequireDeclaration.getType());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrapped = new WrappedBaseRequireModelReferenceField(resolver(), baseRequireDeclaration);
      BaseRequireDeclaration
            xtext =
            WrappedBaseRequireModelReferenceField.toXTextRequireDeclaration(resolver(), wrapped);
      assertEquals("name not correct!",
                   baseRequireDeclaration.getName(),
                   xtext.getName());
      assertEquals("type not correct!",
                   baseRequireDeclaration.getType(),
                   xtext.getType());
   }
}
