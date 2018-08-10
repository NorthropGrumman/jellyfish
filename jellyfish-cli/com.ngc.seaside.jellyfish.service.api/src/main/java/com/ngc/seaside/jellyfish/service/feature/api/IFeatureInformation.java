package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Information about a feature file.
 */
public interface IFeatureInformation {

   /**
    * Returns the path to this feature file
    * 
    * @return the path to this feature file
    */
   Path getPath();

   /**
    * Returns a unique name identifying this feature.
    * 
    * @return a unique name identifying this feature
    */
   String getFullyQualifiedName();

   /**
    * Returns the model associated with this feature file, or {@link Optional#empty()} if there is none.
    * 
    * @return the model associated with this feature file
    */
   Optional<IModel> getModel();

   /**
    * Returns the scenario associated with this feature file, or {@link Optional#empty()} if there is none.
    * 
    * @return the scenario associated with this feature file
    */
   Optional<IScenario> getScenario();
}
