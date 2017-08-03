package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * POJO for a requirement
 */
public class Requirement implements Comparable {
   private String ID;
   private TreeSet<String> features = new TreeSet<>(Collections.reverseOrder());

   public Requirement(String name) {
      this.ID = name;

   }

   /**
    * The ID of this requirement
    *
    * @return feature ID
    */
   public String getID() {
      return ID;
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

      sj.add("\"" + ID + "\"");

      features.forEach(feature ->{
         if (this.features.contains(feature)) {
            sj.add("X");
         }else {
            sj.add("");
         }
      });

      return sj.toString();
   }

   @Override
   public int hashCode() {
      return Objects.hash(ID);
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
      return Objects.equals(ID, that.ID);
   }

   @Override
   public int compareTo(Object obj) throws ClassCastException {
      if (!(obj instanceof Requirement)) {
         throw new ClassCastException("A Requirement object expected.");
      }
      int thatHashCode = obj.hashCode();
      return this.hashCode() - thatHashCode;
   }
}
