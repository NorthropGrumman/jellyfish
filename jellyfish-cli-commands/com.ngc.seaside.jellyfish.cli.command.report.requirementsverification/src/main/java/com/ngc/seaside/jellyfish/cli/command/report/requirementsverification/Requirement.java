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
