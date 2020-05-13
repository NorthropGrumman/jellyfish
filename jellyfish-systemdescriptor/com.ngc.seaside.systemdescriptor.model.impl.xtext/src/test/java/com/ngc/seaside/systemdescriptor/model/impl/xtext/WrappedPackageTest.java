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
package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WrappedPackageTest extends AbstractWrappedXtextTest {

   private WrappedPackage wrapped;

   private Package xtextPackage1;

   private Package xtextPackage2;

   private Package xtextPackage3;

   @Mock
   private ISystemDescriptor parent;

   @Before
   public void setup() throws Throwable {
      Data data = factory().createData();
      data.setName("MyData");

      Model model = factory().createModel();
      model.setName("MyModel");

      Enumeration enumeration = factory().createEnumeration();
      enumeration.setName("MyEnum");

      xtextPackage1 = factory().createPackage();
      xtextPackage2 = factory().createPackage();
      xtextPackage3 = factory().createPackage();
      xtextPackage1.setName("hello.world");
      xtextPackage2.setName(xtextPackage1.getName());
      xtextPackage3.setName(xtextPackage1.getName());
      xtextPackage1.setElement(data);
      xtextPackage2.setElement(model);
      xtextPackage3.setElement(enumeration);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedPackage(resolver(), parent, xtextPackage1);
      wrapped.wrap(xtextPackage2);
      wrapped.wrap(xtextPackage3);
      assertEquals("name not correct!",
                   xtextPackage1.getName(),
                   wrapped.getName());
      assertNotNull("data not correct!",
                    wrapped.getData().getByName("MyData"));
      assertNotNull("models not correct!",
                    wrapped.getModels().getByName("MyModel"));
      assertNotNull("enums not correct!",
                    wrapped.getEnumerations().getByName("MyEnum"));
   }
}
