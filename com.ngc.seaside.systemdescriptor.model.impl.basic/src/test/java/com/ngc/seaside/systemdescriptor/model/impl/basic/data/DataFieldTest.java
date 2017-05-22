package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;

import javax.json.Json;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;
import static org.junit.Assert.assertEquals;

public class DataFieldTest {

  private DataField field;

  private IData parent;

  private IMetadata metadata;

  @Before
  public void setup() throws Throwable {
    parent = new Data("DataType1");
    metadata = new Metadata();
    metadata.getJsonObjects().add(Json.createObjectBuilder().add("foo", "bar").build());
  }

  @Test
  public void testDoesMakeDataField() throws Throwable {
    field = new DataField("field1");
    assertEquals("name not correct!",
                 "field1",
                 field.getName());
  }

  @Test
  public void testDoesMakeImmutableDataField() throws Throwable {
    DataField field = new DataField("field1");
    field.setMetadata(new Metadata());

    IDataField immutable = DataField.immutable(field);
    demandImmutability(() -> immutable.setType(DataTypes.BOOLEAN));
    demandImmutability(() -> immutable.setMetadata(new Metadata()));
  }


}
