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
