package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractWrappedEmfTest {

  @Mock
  private IWrapperResolver resolver;

  private static SystemDescriptorFactory factory;

  @BeforeClass
  public static void setupClass() throws Exception {
    factory = SystemDescriptorFactory.eINSTANCE;
  }

  protected IWrapperResolver resolver() {
    return resolver;
  }

  public static SystemDescriptorFactory factory() {
    return factory;
  }

  public static IMetadata newMetadata(String key, String value) {
    return new WrappedMetadata()
        .setJson(Json.createObjectBuilder().add(key, value).build());
  }
}
