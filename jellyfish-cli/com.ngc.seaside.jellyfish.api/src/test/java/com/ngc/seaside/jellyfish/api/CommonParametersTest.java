/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonParametersTest {

   @Test
   public void testDoesParseGav() throws Throwable {
      String[] parsed = CommonParameters.parseGav("group:artifact:1.0");
      assertEquals("group ID not correct!",
                   "group",
                   parsed[0]);
      assertEquals("artifact ID not correct!",
                   "artifact",
                   parsed[1]);
      assertEquals("version not correct!",
                   "1.0",
                   parsed[2]);
   }

}
