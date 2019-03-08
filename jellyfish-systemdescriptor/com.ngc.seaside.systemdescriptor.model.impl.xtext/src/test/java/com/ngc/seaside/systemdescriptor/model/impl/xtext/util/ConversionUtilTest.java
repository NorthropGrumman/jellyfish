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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.util;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConversionUtilTest {

   @Test
   public void testDoesConvertFromXtext() throws Throwable {
      assertEquals(FieldCardinality.MANY,
                   ConversionUtil.convertCardinalityFromXtext(Cardinality.MANY));
      assertEquals(FieldCardinality.SINGLE,
                   ConversionUtil.convertCardinalityFromXtext(Cardinality.DEFAULT));
   }

   @Test
   public void testDoesConvertToXtext() throws Throwable {
      assertEquals(Cardinality.MANY,
                   ConversionUtil.convertCardinalityToXtext(FieldCardinality.MANY));
      assertEquals(Cardinality.DEFAULT,
                   ConversionUtil.convertCardinalityToXtext(FieldCardinality.SINGLE));
   }
}
