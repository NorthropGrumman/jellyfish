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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class WrappedEnumerationTest extends AbstractWrappedXtextTest {

   private WrappedEnumeration wrappedEnumeration;

   private Enumeration enumeration;

   @Mock
   private IPackage parent;

   @Before
   public void setup() throws Throwable {
      enumeration = factory().createEnumeration();
      enumeration.setName("MyEnum");

      EnumerationValueDeclaration value = factory().createEnumerationValueDeclaration();
      value.setValue("VALUE1");
      enumeration.getValues().add(value);

      Package p = factory().createPackage();
      p.setName("my.package");
      p.setElement(enumeration);
      when(resolver().getWrapperFor(p)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedEnumeration = new WrappedEnumeration(resolver(), enumeration);
      assertEquals("name not correct!",
                   enumeration.getName(),
                   wrappedEnumeration.getName());
      assertEquals("fully qualified name not correct!",
                   "my.package.MyEnum",
                   wrappedEnumeration.getFullyQualifiedName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedEnumeration.getParent());
      assertEquals("metadata not set!",
                   IMetadata.EMPTY_METADATA,
                   wrappedEnumeration.getMetadata());

      String value = enumeration.getValues().get(0).getValue();
      assertEquals("value not correct!",
                   value,
                   wrappedEnumeration.getValues().iterator().next());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrappedEnumeration = new WrappedEnumeration(resolver(), enumeration);

      wrappedEnumeration.setMetadata(newMetadata("foo", "bar"));
      assertNotNull("metadata not set!",
                    enumeration.getMetadata());
   }
}
