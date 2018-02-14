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
      wrappedEnumeration.getValues().add("VALUE2");
      assertEquals("new field name not correct!",
                   "VALUE2",
                   enumeration.getValues().get(1).getValue());

      wrappedEnumeration.setMetadata(newMetadata("foo", "bar"));
      assertNotNull("metadata not set!",
                    enumeration.getMetadata());
   }
}
