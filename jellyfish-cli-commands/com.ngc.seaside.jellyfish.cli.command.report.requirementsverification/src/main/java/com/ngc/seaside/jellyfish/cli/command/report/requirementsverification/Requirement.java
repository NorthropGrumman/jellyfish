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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * POJO for a requirement
 */
public class Requirement implements Comparable<Requirement> {
   private String id;
   private TreeSet<String> features = new TreeSet<>(Collections.reverseOrder());

   public Requirement(String name) {
      this.id = name;

   }

   /**
    * The ID of this requirement
    *
    * @return feature ID
    */
   public String getID() {
      return id;
   }

   /**
    * Returns a set of features satisfies this requirement
    *
    * @return features belonging by this requirement
    */
   public TreeSet<String> getFeatures() {
      return features;
   }

   /**
    * Adds a feature to the requirement
    *
    * @param feature feature to add
    */
   void addFeature(String feature) {
      this.features.add(feature);
   }

   /**
    * Adds a collection of features to the requirement
    *
    * @param features requirements to add
    */
   void addFeatures(Collection<String> features) {
      features.forEach(this::addFeature);
   }

   /**
    * Creates a comma delimited features verification string
    * @param features the features to be verified
    * @return a comma delimited string of feature verification
    */
   public String createFeatureVerificationCsvString(Collection<String> features) {
      StringJoiner sj = new StringJoiner(",");

      sj.add("\"" + id + "\"");

      features.forEach(feature -> {
         if (this.features.contains(feature)) {
            sj.add("X");
         } else {
            sj.add("");
         }
      });

      return sj.toString();
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (!(obj instanceof Requirement)) {
         return false;
      }
      Requirement that = (Requirement) obj;
      return Objects.equals(id, that.id);
   }

   @Override
   public int compareTo(Requirement obj) throws ClassCastException {
      int thatHashCode = obj.hashCode();
      return this.hashCode() - thatHashCode;
   }
}
