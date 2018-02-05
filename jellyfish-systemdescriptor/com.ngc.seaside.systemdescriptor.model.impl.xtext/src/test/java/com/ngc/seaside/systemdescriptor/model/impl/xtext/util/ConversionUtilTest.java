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
