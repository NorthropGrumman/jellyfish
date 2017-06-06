package com.ngc.seaside.systemdescriptor.scenario.api;

import java.util.Objects;

public class ScenarioStepVerb {

   private final String pastTense;
   private final String currentTense;
   private final String futureTense;

   private ScenarioStepVerb(String pastTense,
                            String currentTense,
                            String futureTense) {
      this.pastTense = pastTense;
      this.currentTense = currentTense;
      this.futureTense = futureTense;
   }

   public String getPastTense() {
      return pastTense;
   }

   public String getCurrentTense() {
      return currentTense;
   }

   public String getFutureTense() {
      return futureTense;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ScenarioStepVerb)) {
         return false;
      }
      ScenarioStepVerb that = (ScenarioStepVerb) o;
      return Objects.equals(pastTense, that.pastTense) &&
             Objects.equals(currentTense, that.currentTense) &&
             Objects.equals(futureTense, that.futureTense);
   }

   @Override
   public int hashCode() {
      return Objects.hash(pastTense, currentTense, futureTense);
   }

   @Override
   public String toString() {
      return currentTense;
   }

   public static ScenarioStepVerb create(String pastTense,
                                         String currentTense,
                                         String futureTense) {
      if (pastTense == null) {
         throw new NullPointerException("pastTense may not be null!");
      }
      if (pastTense.trim().isEmpty()) {
         throw new IllegalArgumentException("pastTense may not be empty!");
      }
      if (currentTense == null) {
         throw new NullPointerException("currentTense may not be null!");
      }
      if (currentTense.trim().isEmpty()) {
         throw new IllegalArgumentException("currentTense may not be empty!");
      }
      if (futureTense == null) {
         throw new NullPointerException("futureTense may not be null!");
      }
      if (futureTense.trim().isEmpty()) {
         throw new IllegalArgumentException("futureTense may not be empty!");
      }
      return new ScenarioStepVerb(pastTense, currentTense, futureTense);
   }
}
