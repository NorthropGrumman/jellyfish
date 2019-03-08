/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.scenario.api;

import java.util.Objects;

/**
 * Stores a form of a verb and its tense.  This class can be used as shown below to present the 3 tenses of the verb
 * "publish":
 * <pre>
 *    {@code
 *      ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("havePublished");
 *      ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("publishing");
 *      ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willPublish");
 *    }
 * </pre>
 */
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
      return tense == that.tense
            && Objects.equals(verb, that.verb);
   }

   @Override
   public int hashCode() {
      return Objects.hash(tense, verb);
   }

   /**
    * Creates a verb in past tense.
    *
    * @see #create(String, VerbTense)
    */
   public static ScenarioStepVerb pastTense(String verb) {
      return create(verb, VerbTense.PAST_TENSE);
   }

   /**
    * Creates a verb in present tense.
    *
    * @see #create(String, VerbTense)
    */
   public static ScenarioStepVerb presentTense(String verb) {
      return create(verb, VerbTense.PRESENT_TENSE);
   }

   /**
    * Creates a verb in future tense.
    *
    * @see #create(String, VerbTense)
    */
   public static ScenarioStepVerb futureTense(String verb) {
      return create(verb, VerbTense.FUTURE_TENSE);
   }

   /**
    * Creates a verb in the given tense.
    *
    * @param verb  the actual verb
    * @param tense the tense of the verb
    * @throws NullPointerException     {@code verb} or {@code tense} is {@code null}
    * @throws IllegalArgumentException {@code verb} is an empty string
    */
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
