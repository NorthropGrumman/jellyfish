package com.ngc.seaside.systemdescriptor.model.impl.basic;

import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;

public class PackageTest {

   private Package p;
   
   @Before
   public void setup() {
    
   }
   
   public void testDoesImplementIPackage() {
      p = new Package("foo.bar");
      assertEquals("name not set!",
         "foo.bar",
         p.getName());
      
      IData data = mock(IData.class);
      when(data.getName()).thenReturn("MyData");
      p.getData().add(data);
      assertEquals("didn't add data!",
         data,
         p.getData().getByName(data.getName()));
      
   }
   
}
