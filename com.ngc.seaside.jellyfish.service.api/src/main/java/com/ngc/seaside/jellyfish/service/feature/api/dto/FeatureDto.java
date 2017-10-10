package com.ngc.seaside.jellyfish.service.feature.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeSet;

public class FeatureDto implements Comparable<FeatureDto> {
   private String fileName;
   private String fullyQualifiedName;
   private String name;
   private TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

   public FeatureDto(String qualifiedName, String name) {
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

      if (!(obj instanceof FeatureDto)) {
         return false;
      }
      FeatureDto that = (FeatureDto) obj;
      return Objects.equals(fileName, that.fileName) &&
             Objects.equals(fullyQualifiedName, that.fullyQualifiedName) &&
             Objects.equals(name, that.name);
   }

   @Override
   public int compareTo(FeatureDto obj) {
      int thatHashCode = obj.hashCode();
      return this.hashCode() - thatHashCode;
   }
}
