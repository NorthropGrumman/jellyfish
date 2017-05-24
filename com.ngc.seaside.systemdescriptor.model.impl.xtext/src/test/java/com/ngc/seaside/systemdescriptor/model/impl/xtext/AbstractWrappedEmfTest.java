package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.junit.BeforeClass;

public abstract class AbstractWrappedEmfTest {

  private static SystemDescriptorFactory factory;

  @BeforeClass
  public static void setupClass() throws Exception {
    factory = SystemDescriptorFactory.eINSTANCE;
  }

  public SystemDescriptorFactory factory() {
    return getFactory();
  }

  public static SystemDescriptorFactory getFactory() {
    return factory;
  }
}
