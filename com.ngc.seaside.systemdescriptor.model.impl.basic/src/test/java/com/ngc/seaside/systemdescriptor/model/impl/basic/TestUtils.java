package com.ngc.seaside.systemdescriptor.model.impl.basic;

import static org.junit.Assert.fail;

public class TestUtils {

  private TestUtils() {
  }

  public static void demandImmutability(Runnable r) {
    try {
      r.run();
      fail("did not throw UnsupportedOperationException!");
    } catch (UnsupportedOperationException e) {
      // Expected.
    }
  }
}
