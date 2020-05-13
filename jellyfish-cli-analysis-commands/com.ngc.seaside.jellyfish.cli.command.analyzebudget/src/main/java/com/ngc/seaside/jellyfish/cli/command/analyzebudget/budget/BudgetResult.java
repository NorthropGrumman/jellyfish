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

import java.util.Comparator;
import java.util.Objects;

import javax.measure.Quantity;

/**
 * Results of a budget, including the actual total.
 * 
 * @param <T> unit of the budget
 */
public class BudgetResult<T extends Quantity<T>> {

   private final Budget<T> budget;
   private final Quantity<T> actual;
   private final boolean above;
   private final boolean below;

   /**
    * Constructs a budget result.
    * 
    * @param budget original budget
    * @param actual actual total
    */
   public BudgetResult(Budget<T> budget, Quantity<T> actual) {
      this.budget = budget;
      this.actual = actual;
      this.below = compare(budget.getMinimum(), actual) > 0;
      this.above = compare(actual, budget.getMaximum()) > 0;
   }

   /**
    * Returns the original budget.
    * 
    * @return the original budget
    */
   public Budget<T> getBudget() {
      return budget;
   }

   /**
    * Returns the actual total.
    * 
    * @return the actual total
    */
   public Quantity<T> getActual() {
      return actual;
   }

   /**
    * Returns {@code true} if the actual total was within budget.
    * 
    * @return {@code true} if the actual total was within budget
    */
   public boolean withinBudget() {
      return !below && !above;
   }

   /**
    * Returns {@code true} if the actual total was above the budget maximum.
    * 
    * @return {@code true} if the actual total was above the budget maximum
    */
   public boolean overBudget() {
      return above;
   }

   /**
    * Returns {@code true} if the actual total was below the budget minimum.
    * 
    * @return {@code true} if the actual total was below the budget minimum
    */
   public boolean underBudget() {
      return below;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof BudgetResult)) {
         return false;
      }
      BudgetResult<?> that = (BudgetResult<?>) o;
      return Objects.equals(this.budget, that.budget)
               && Objects.equals(this.actual, that.actual);
   }

   @Override
   public int hashCode() {
      return Objects.hash(budget, actual);
   }

   @Override
   public String toString() {
      return "BudgetResult[budget=" + budget + ",actual=" + actual + "]";
   }

   /**
    * Compares two quantities.
    * 
    * @param q1 first quantity
    * @param q2 second quantity
    * @return the {@link Comparator#compare(Object, Object) comparison} of the two quantities.
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   private static <T extends Quantity<T>> int compare(Quantity<T> q1, Quantity<T> q2) {
      if (q1 instanceof Comparable) {
         return ((Comparable) q1).compareTo(q2);
      }
      return Double.compare(q1.to(q1.getUnit().getSystemUnit()).getValue().doubleValue(),
               q2.to(q1.getUnit().getSystemUnit()).getValue().doubleValue());
   }
}
