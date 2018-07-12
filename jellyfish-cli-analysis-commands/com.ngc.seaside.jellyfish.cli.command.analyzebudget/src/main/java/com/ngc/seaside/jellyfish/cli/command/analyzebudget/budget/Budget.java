package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

import com.google.common.base.Preconditions;

import java.util.Objects;

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
    * @param source the source code reference to the budget, can be null
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Budget(Quantity<Q> minimum, Quantity<Q> maximum, String property, Object source) {
      Preconditions.checkNotNull(minimum, "minimum may not be null");
      Preconditions.checkNotNull(maximum, "maximum may not be null");
      Preconditions.checkArgument(((Comparable) minimum).compareTo(maximum) <= 0, "minimum must be less than maximum");
      Preconditions.checkArgument(property != null && !property.isEmpty(), "property may not be null or empty");
      Preconditions.checkArgument(Objects.equals(minimum.getUnit(), maximum.getUnit()));
      this.minimum = minimum;
      this.maximum = maximum;
      this.property = property;
      this.source = source;
   }

   public Unit<Q> getUnit() {
      return minimum.getUnit();
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
