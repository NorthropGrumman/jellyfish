package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeSet;

/**
 * POJO for a feature
 */
public class Feature implements Comparable{
   private String fileName;
   private String fullyQualifiedName;
   private String name;
   private TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

   public Feature(String qualifiedName, String name) {
      this.fileName = qualifiedName.concat(".feature");
      this.fullyQualifiedName = qualifiedName;
      this.name = name;
   }

   /**
    * File name of feature file
    *
    * @return file name of feature file
    */
   String getFileName() {
      return fileName;
   }

   /**
    * A qualified name is equal to "modelName.featureName"
    *
    * @return fully qualified name of feature
    */
   String getFullyQualifiedName() {
      return fullyQualifiedName;
   }

   /**
    * The name of this feature
    *
    * @return feature name
    */
   String getName() {
      return name;
   }

   /**
    * Returns a set of requirements that is belonging to this feature
    *
    * @return requirements belonging by this feature
    */
   Collection<String> getRequirements() {
      return requirements;
   }

   /**
    * Adds a requirement to the feature
    *
    * @param requirement requirement to add
    */
   void addRequirement(String requirement) {
      this.requirements.add(requirement);
   }

   /**
    * Adds a collection of requirements to the feature
    *
    * @param requirements requirements to add
    */
   void addRequirements(Collection<String> requirements) {
      requirements.forEach(this::addRequirement);
   }
   @Override
   public int hashCode() {
      return Objects.hash(fullyQualifiedName);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (!(obj instanceof Feature)) {
         return false;
      }
      Feature that = (Feature) obj;
      return Objects.equals(fileName, that.fileName) &&
             Objects.equals(fullyQualifiedName, that.fullyQualifiedName) &&
             Objects.equals(name, that.name);
   }

   @Override
   public int compareTo(Object obj) throws ClassCastException {
      if (!(obj instanceof Feature)) {
         throw new ClassCastException("A Feature object expected.");
      }
      int thatHashCode = obj.hashCode();
      return this.hashCode() - thatHashCode;
   }
}
