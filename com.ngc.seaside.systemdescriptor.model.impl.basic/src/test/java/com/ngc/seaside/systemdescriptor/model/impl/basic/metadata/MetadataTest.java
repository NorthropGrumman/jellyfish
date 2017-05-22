package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import org.junit.Test;

import javax.json.spi.JsonProvider;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;

public class MetadataTest {

  @Test
  public void testDoesMakeImmutable() throws Throwable {
    Metadata data = new Metadata();
    data.getJsonObjects().add(JsonProvider.provider().createObjectBuilder().add("foo", "bar").build());

    IMetadata immutable = Metadata.immutable(data);
    demandImmutability(() -> immutable.getJsonObjects().add(JsonProvider.provider()
                                                                .createObjectBuilder()
                                                                .add("foo", "bar").build()));
  }
}
