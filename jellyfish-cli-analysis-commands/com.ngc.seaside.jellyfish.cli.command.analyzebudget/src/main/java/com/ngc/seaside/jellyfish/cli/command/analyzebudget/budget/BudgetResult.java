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
