package com.ngc.seaside.jellyfish.service.impl.propertyservice;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 *
 */
public class StringUtilitiesTest {

   @Test
   public void doesReplaceAllUppercase() {
      String className = "MyClassName";

      String value = StringUtilities.replaceCamelWithHyphens(className);

      assertEquals("my-class-name", value);
   }

}
