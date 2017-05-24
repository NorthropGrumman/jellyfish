package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedEmfTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class WrappedDataFieldTest extends AbstractWrappedEmfTest {

  private WrappedDataField wrapped;

  private DataFieldDeclaration dataFieldDeclaration;

  @Mock
  private IData parent;

  @Before
  public void setup() throws Throwable {
    dataFieldDeclaration = factory().createDataFieldDeclaration();
    dataFieldDeclaration.setName("foo");
    dataFieldDeclaration.setType(DataType.INT);
  }

  @Test
  public void testDoesWrapEmfObject() throws Throwable {
    wrapped = new WrappedDataField(dataFieldDeclaration, parent);
    assertEquals("name incorrect!",
                 dataFieldDeclaration.getName(),
                 wrapped.getName());
    assertEquals("type incorrect!",
                 DataTypes.INT,
                 wrapped.getType());
    assertEquals("parent incorrect!",
                 parent,
                 wrapped.getParent());
    assertEquals("metadata not set!",
                 IMetadata.EMPTY_METADATA,
                 wrapped.getMetadata());
  }

  @Test
  public void testDoesUpdateEmpObject() throws Throwable {
    wrapped = new WrappedDataField(dataFieldDeclaration, parent);
    wrapped.setType(DataTypes.STRING);
    assertEquals("type incorrect!",
                 DataType.STRING,
                 dataFieldDeclaration.getType());

    wrapped.setMetadata(newMetadata("foo", "bar"));
    assertNotNull("metadata not set!",
                  dataFieldDeclaration.getMetadata());
  }
}
