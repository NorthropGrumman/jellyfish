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
