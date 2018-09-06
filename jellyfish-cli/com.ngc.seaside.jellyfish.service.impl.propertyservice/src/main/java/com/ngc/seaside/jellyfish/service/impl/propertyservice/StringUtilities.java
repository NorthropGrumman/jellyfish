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
package com.ngc.seaside.jellyfish.service.impl.propertyservice;

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
