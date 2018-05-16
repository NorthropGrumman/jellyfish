package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionUtilTest {

   @Test
   public void testDoesGetVersion() throws Throwable {
      assertEquals("failed to get current Jellyfish version!",
                   "1.2.3",
                   VersionUtil.getCurrentJellyfishVersion());
   }
}
