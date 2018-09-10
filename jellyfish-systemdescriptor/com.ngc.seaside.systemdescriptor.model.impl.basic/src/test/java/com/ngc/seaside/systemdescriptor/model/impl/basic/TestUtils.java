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
package com.ngc.seaside.systemdescriptor.model.impl.basic;

import static org.junit.Assert.fail;

public class TestUtils {

   private TestUtils() {
   }

   /**
    * Asserts that running the given runnable throws an {@code UnsupportedOperationException}.
    */
   public static void demandImmutability(Runnable r) {
      try {
         r.run();
         fail("did not throw UnsupportedOperationException!");
      } catch (UnsupportedOperationException e) {
         // Expected.
      }
   }
}
