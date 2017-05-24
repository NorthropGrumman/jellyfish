package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.junit.BeforeClass;

import javax.json.Json;

public abstract class AbstractWrappedEmfTest {

  private static SystemDescriptorFactory factory;

  @BeforeClass
  public static void setupClass() throws Exception {
    factory = SystemDescriptorFactory.eINSTANCE;
  }

  public static SystemDescriptorFactory factory() {
    return factory;
  }

  public static IMetadata newMetadata(String key, String value) {
    return new WrappedMetadata()
        .setJson(Json.createObjectBuilder().add(key, value).build());
  }
}
