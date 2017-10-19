package com.ngc.seaside.jellyfish.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CommonParametersTest {

   @Test
   public void testDoesParseGave() throws Throwable {
      String[] parsed = CommonParameters.parseGave("group:artifact:1.0@zip");
      assertEquals("group ID not correct!",
                   "group",
                   parsed[0]);
      assertEquals("artifact ID not correct!",
                   "artifact",
                   parsed[1]);
      assertEquals("version not correct!",
                   "1.0",
                   parsed[2]);
      assertEquals("extension not correct!",
                   "zip",
                   parsed[3]);
   }

   @Test
   public void testDoesThrowIAEIfGaveIsInvalid() throws Throwable {
      try {
         CommonParameters.parseGave("group:artifact:1.0");
         fail("failed to detect missing extension!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         CommonParameters.parseGave("group:artifact:1.0:zip");
         fail("failed to detect wrong separator extension!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         CommonParameters.parseGave(":artifact:1.0@zip");
         fail("failed to detect missing group!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         CommonParameters.parseGave("group::1.0@zip");
         fail("failed to detect missing artifact!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         CommonParameters.parseGave("group:artifact@zip");
         fail("failed to detect missing version!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }

      try {
         CommonParameters.parseGave("::@");
         fail("failed to detect missing values!");
      } catch (IllegalArgumentException e) {
         // Expected.
      }
   }
}
