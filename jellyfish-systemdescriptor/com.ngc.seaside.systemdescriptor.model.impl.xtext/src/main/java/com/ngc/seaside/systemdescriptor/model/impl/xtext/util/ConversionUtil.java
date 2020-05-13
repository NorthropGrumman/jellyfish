/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
