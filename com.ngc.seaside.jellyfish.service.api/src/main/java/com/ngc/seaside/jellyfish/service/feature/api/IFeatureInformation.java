package com.ngc.seaside.jellyfish.service.feature.api;

import java.util.Collection;

public interface IFeatureInformation {
   /**
    * File name of feature file
    *
    * @return file name of feature file
    */
   String getFileName();

   /**
    * A qualified name is equal to "modelName.featureName"
    *
    * @return fully qualified name of feature
    */
   String getFullyQualifiedName();

   /**
    * The name of this feature
    *
    * @return feature name
    */
   String getName();

   /**
    * Returns a set of requirements that is belonging to this feature
    *
    * @return requirements belonging by this feature
    */
   Collection<String> getRequirements();

}
