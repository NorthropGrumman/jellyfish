package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class FeatureInformation implements IFeatureInformation {
   private String fileName;
   private String fullyQualifiedName;
   private String name;
   private TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

   @Override
   public String getFileName() {
      return fileName;
   }
   
   /**
    * Set the file name of feature file
    * @param fileName the file name
    * @return the feature information
    */
   public FeatureInformation setFileName(String fileName) {
      this.fileName = fileName;
      return this;
   }

   @Override
   public String getFullyQualifiedName() {
      return fullyQualifiedName;
   }
   
   /**
    * Set the fully qualified name
    * @param fullyQualifiedName the fully qualified name
    * @return the feature information
    */
   public FeatureInformation setFullyQualifiedName(String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
      return this;
   }

   @Override
   public String getName() {
      return name;
   }
   
   /**
    * Set the name of this feature
    * @param name the name
    * @return the feature information
    */
   public FeatureInformation setName(String name) {
      this.name = name;
      return this;
   }

   @Override
   public Collection<String> getRequirements() {
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
   public void addRequirements(Collection<String> requirements) {
      requirements.forEach(this::addRequirement);
   }

}
