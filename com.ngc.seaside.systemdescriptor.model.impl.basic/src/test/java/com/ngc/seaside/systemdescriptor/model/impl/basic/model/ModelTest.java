package com.ngc.seaside.systemdescriptor.model.impl.basic.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;

import org.junit.Before;

public class ModelTest {

   private Model m;
   
   @Before
   public void setup() {
    
   }
   
   public void testDoesImplementIPackage() {
      IPackage pkg = mock(IPackage.class);
      when(pkg.getName()).thenReturn("com.ngc.seaside.mypackage");
      m = new Model("foo.bar");
      assertEquals("name not set!",
         "foo.bar",
         m.getName());
      
      m.setParent(pkg);
      assertEquals("fullyqualifiedname not set!",
         "com.ngc.seaside.mypackage.foo.bar",
         m.getFullyQualifiedName());
      
   }
}
