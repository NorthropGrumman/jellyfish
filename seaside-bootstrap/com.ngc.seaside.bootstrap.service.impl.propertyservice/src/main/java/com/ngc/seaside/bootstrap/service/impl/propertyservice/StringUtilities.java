package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.common.base.CaseFormat;

/**
 *
 */
public class StringUtilities {

   /**
    * Replace a camel case String with the lowercase hyphen version.
    * MyClass would then be my-class
    *
    * @param name the property to replace
    * @return the replaced version
    */
   public static String replaceCamelWithHyphens(String name) {
      return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name);
   }

}
