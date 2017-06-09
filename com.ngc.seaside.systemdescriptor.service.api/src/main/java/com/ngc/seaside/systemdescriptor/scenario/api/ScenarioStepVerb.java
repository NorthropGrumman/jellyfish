package com.ngc.seaside.systemdescriptor.scenario.api;

import java.util.Objects;

public class ScenarioStepVerb {

   private final String verb;
   private final VerbTense tense;

   private ScenarioStepVerb(String verb, VerbTense tense) {
      this.verb = verb;
      this.tense = tense;
   }

   public String getVerb() {
      return verb;
   }

   public VerbTense getTense() {
      return tense;
   }

   @Override
   public String toString() {
      return verb;
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
      return tense == that.tense &&
             Objects.equals(verb, that.verb);
   }

   @Override
   public int hashCode() {
      return Objects.hash(tense, verb);
   }

   public static ScenarioStepVerb pastTense(String verb) {
      return create(verb, VerbTense.PAST_TENSE);
   }

   public static ScenarioStepVerb presentTense(String verb) {
      return create(verb, VerbTense.PRESENT_TENSE);
   }

   public static ScenarioStepVerb futureTense(String verb) {
      return create(verb, VerbTense.FUTURE_TENSE);
   }

   public static ScenarioStepVerb create(String verb, VerbTense tense) {
      if (verb == null) {
         throw new NullPointerException("verb may not be null!");
      }
      if (verb.trim().isEmpty()) {
         throw new IllegalArgumentException("verb may not be empty!");
      }
      if (tense == null) {
         throw new NullPointerException("tense may not be null!");
      }
      return new ScenarioStepVerb(verb, tense);
   }
}
