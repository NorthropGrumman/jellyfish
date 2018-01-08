package com.ngc.seaside.jellyfish.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
