package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedDataFieldTest extends AbstractWrappedXtextTest {

   private WrappedDataField wrapped;

   private DataFieldDeclaration dataFieldDeclaration;

   @Mock
   private IData parent;

   @Before
   public void setup() throws Throwable {
      dataFieldDeclaration = factory().createDataFieldDeclaration();
      dataFieldDeclaration.setName("foo");

      Data data = factory().createData();
      data.getFields().add(dataFieldDeclaration);
      when(resolver().getWrapperFor(data)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedDataField(resolver(), dataFieldDeclaration);
      assertEquals("name incorrect!",
                   dataFieldDeclaration.getName(),
                   wrapped.getName());
      assertEquals("parent incorrect!",
                   parent,
                   wrapped.getParent());
      assertEquals("metadata not set!",
                   IMetadata.EMPTY_METADATA,
                   wrapped.getMetadata());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrapped = new WrappedDataField(resolver(), dataFieldDeclaration);

      wrapped.setMetadata(newMetadata("foo", "bar"));
      assertNotNull("metadata not set!",
                    dataFieldDeclaration.getMetadata());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      IDataField field = mock(IDataField.class);
      when(field.getName()).thenReturn("hello");
      when(field.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);

      DataFieldDeclaration x = WrappedDataField.toXtext(field);
      assertEquals("name not correct!",
                   field.getName(),
                   x.getName());
      assertNull("metadata not correct!",
                 x.getMetadata());
   }
}
