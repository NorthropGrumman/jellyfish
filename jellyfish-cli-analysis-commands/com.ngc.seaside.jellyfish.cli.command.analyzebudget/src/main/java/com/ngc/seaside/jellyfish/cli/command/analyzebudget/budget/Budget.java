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
package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.function.Function;

import javax.measure.Quantity;
import javax.measure.Unit;

public class Budget<Q extends Quantity<Q>> {

   private final Quantity<Q> minimum;
   private final Quantity<Q> maximum;
   private final String property;
   private final Object source;

   /**
    * Constructs a budget.
    * 
    * @param minimum budget minimum
    * @param maximum budget maximum
    * @param property budget property name
    * @param srcFunction if an error occurs, this function will be used to convert one of the input parameters to its
    *           error source. A null input should return the source of the budget itself.
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Budget(Quantity<Q> minimum, Quantity<Q> maximum, String property, Function<Object, Object> srcFunction) {
      Preconditions.checkNotNull(minimum, "minimum may not be null");
      Preconditions.checkNotNull(maximum, "maximum may not be null");
      Preconditions.checkArgument(property != null && !property.isEmpty(), "property may not be null or empty");
      if (!minimum.getUnit().isCompatible(maximum.getUnit())) {
         throw new BudgetValidationException("min and max must use the same unit", null, srcFunction.apply(minimum));
      }
      if (((Comparable) minimum).compareTo(maximum) > 0) {
         throw new BudgetValidationException("minimum must be less that maximum", null, srcFunction.apply(maximum));
      }
      this.minimum = minimum;
      this.maximum = maximum;
      this.property = property;
      this.source = srcFunction.apply(null);
   }

   public Unit<Q> getUnit() {
      return maximum.getUnit();
   }

   public Quantity<Q> getMinimum() {
      return minimum;
   }

   public Quantity<Q> getMaximum() {
      return maximum;
   }

   public String getProperty() {
      return property;
   }

   public Object getSource() {
      return source;
   }

   @Override
   public String toString() {
      return String.format("Budget[minimum=%s,maximum=%s,property=%s", minimum, maximum, property);
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof Budget)) {
         return false;
      }
      Budget<?> that = (Budget<?>) o;
      return Objects.equals(this.minimum, that.minimum)
               && Objects.equals(this.maximum, that.maximum)
               && Objects.equals(this.property, that.property);
   }

   @Override
   public int hashCode() {
      return Objects.hash(minimum, maximum, property);
   }
}
