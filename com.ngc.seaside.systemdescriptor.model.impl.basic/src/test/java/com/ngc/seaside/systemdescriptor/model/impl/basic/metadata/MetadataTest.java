package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import org.junit.Test;

import javax.json.Json;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;

public class MetadataTest {

  @Test
  public void testDoesMakeImmutable() throws Throwable {
    Metadata data = new Metadata();
    data.setJson(Json.createObjectBuilder().add("foo", "bar").build());

    IMetadata immutable = Metadata.immutable(data);
    demandImmutability(() -> immutable.setJson(Json.createObjectBuilder()
                                                   .add("foo", "bar").build()));
  }
}
