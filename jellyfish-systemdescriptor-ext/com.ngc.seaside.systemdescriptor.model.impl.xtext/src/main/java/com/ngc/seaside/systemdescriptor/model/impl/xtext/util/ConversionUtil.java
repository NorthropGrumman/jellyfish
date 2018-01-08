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
