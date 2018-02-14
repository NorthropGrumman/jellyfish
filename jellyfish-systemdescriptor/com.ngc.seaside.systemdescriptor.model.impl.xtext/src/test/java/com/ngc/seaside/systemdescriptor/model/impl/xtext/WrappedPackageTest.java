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
