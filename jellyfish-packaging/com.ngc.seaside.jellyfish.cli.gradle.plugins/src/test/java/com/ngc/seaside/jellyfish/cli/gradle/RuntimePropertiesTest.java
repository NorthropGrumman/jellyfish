package com.ngc.seaside.jellyfish.cli.gradle;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RuntimePropertiesTest {

   @Test
   public void testDoesLoadProperties() {
      String version = RuntimeProperties.getVersion();
      String group = RuntimeProperties.getGroup();
      // Don't assert the actual value here.  This is because the value can be different depending on the build
      // properties.  We just want to make sure the values are set.
      assertNotNull("version property not set!", version);
      assertNotNull("group property not set!", group);
   }
}
