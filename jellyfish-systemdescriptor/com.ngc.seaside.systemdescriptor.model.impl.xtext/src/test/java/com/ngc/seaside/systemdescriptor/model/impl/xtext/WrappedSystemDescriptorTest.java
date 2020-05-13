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

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedSystemDescriptorTest extends AbstractWrappedXtextTest {

   private WrappedSystemDescriptor wrapped;

   private Package xtextPackage1;

   private Package xtextPackage2;

   @Mock
   private ResourceSet resourceSet;

   @Before
   public void setup() throws Throwable {
      Data data = factory().createData();
      data.setName("MyData");

      Model model = factory().createModel();
      model.setName("MyModel");

      xtextPackage1 = factory().createPackage();
      xtextPackage2 = factory().createPackage();
      xtextPackage1.setName("hello.world");
      xtextPackage2.setName("hello.world.again");
      xtextPackage1.setElement(data);
      xtextPackage2.setElement(model);

      Resource resource1 = mock(Resource.class);
      Resource resource2 = mock(Resource.class);

      when(resourceSet.getResources()).thenReturn(ECollections.asEList(resource1, resource2));
      when(resource1.getContents()).thenReturn(ECollections.asEList(xtextPackage1));
      when(resource2.getContents()).thenReturn(ECollections.asEList(xtextPackage2));

      wrapped = new WrappedSystemDescriptor(xtextPackage1) {
         @Override
         protected ResourceSet doGetResourceSet(EObject object) {
            return resourceSet;
         }
      };
   }

   @Test
   public void testDoesWrapAllPackages() throws Throwable {
      assertTrue("did not wrap package!",
                 wrapped.getPackages().getByName(xtextPackage1.getName()).isPresent());
      assertTrue("did not wrap package!",
                 wrapped.getPackages().getByName(xtextPackage2.getName()).isPresent());
   }

   @Test
   public void testDoesFindData() throws Throwable {
      Optional<IData> data = wrapped.findData(xtextPackage1.getName(), "MyData");
      assertTrue("did not find data with package and name!",
                 data.isPresent());

      data = wrapped.findData(xtextPackage1.getName(), "MyMissingData");
      assertFalse("data should not be found!",
                  data.isPresent());

      data = wrapped.findData(xtextPackage1.getName() + ".MyData");
      assertTrue("did not find data with fully qualified name!",
                 data.isPresent());

      try {
         wrapped.findData(".MyData");
         fail("did not detect illegal fully qualified name!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         wrapped.findData("MyData");
         fail("did not detect illegal fully qualified name!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }
   }

   @Test
   public void testDoesFindModel() throws Throwable {
      Optional<IModel> model = wrapped.findModel(xtextPackage2.getName(), "MyModel");
      assertTrue("did not find model with package and name!",
                 model.isPresent());

      model = wrapped.findModel(xtextPackage2.getName(), "MyMissingData");
      assertFalse("model should not be found!",
                  model.isPresent());

      model = wrapped.findModel(xtextPackage2.getName() + ".MyModel");
      assertTrue("did not find model with fully qualified name!",
                 model.isPresent());

      try {
         wrapped.findModel(".MyModel");
         fail("did not detect illegal fully qualified name!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         wrapped.findModel("MyModel");
         fail("did not detect illegal fully qualified name!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }
   }
}
