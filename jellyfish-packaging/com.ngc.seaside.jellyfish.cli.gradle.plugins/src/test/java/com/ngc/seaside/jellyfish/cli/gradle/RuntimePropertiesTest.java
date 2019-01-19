/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
