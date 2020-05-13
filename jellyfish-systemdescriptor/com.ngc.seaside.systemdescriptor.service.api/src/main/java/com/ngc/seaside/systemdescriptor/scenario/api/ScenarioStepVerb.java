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
