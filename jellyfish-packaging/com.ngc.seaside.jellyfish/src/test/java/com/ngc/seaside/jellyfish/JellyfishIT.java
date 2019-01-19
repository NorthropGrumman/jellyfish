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
package com.ngc.seaside.jellyfish;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JellyfishIT {

   @Before
   public void setup() {
      String ngFwHome = Paths.get(System.getProperty("user.dir"),
                                  "build",
                                  "resources",
                                  "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
   }

   @Test
   public void validSDProjectParsedTest() {
      Jellyfish.main(new String[]{"help"});
      Jellyfish.main(new String[]{"help", "-Dverbose=true"});
   }

   @After
   public void after() {
      System.clearProperty("NG_FW_HOME");
   }
}
