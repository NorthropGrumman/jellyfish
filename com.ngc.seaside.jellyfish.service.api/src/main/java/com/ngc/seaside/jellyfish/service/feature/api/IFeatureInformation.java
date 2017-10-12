package com.ngc.seaside.jellyfish.service.feature.api;

import java.nio.file.Path;
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
    * Returns the relative path to this feature file
    * 
    * @return the relative path
    */
   Path getRelativePath();

   /**
    * Returns the absolute path to this feature file
    * 
    * @return the absolute path
    */
   Path getAbsolutePath();
}
