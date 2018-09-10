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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.util;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnconvertableTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;

/**
 * Contains simple conversion utils.
 */
public class ConversionUtil {

   /**
    * Converts {@code FieldCardinality} to {@code Cardinality}.
    */
   public static Cardinality convertCardinalityToXtext(FieldCardinality cardinality) {
      switch (cardinality) {
         case SINGLE:
            return Cardinality.DEFAULT;
         case MANY:
            return Cardinality.MANY;
         default:
            throw new UnconvertableTypeException(cardinality);
      }
   }

   /**
    * Converts {@code Cardinality} to {@code FieldCardinality}.
    */
   public static FieldCardinality convertCardinalityFromXtext(Cardinality cardinality) {
      switch (cardinality) {
         case DEFAULT:
            return FieldCardinality.SINGLE;
         case MANY:
            return FieldCardinality.MANY;
         default:
            throw new UnrecognizedXtextTypeException(cardinality);
      }
   }
}
