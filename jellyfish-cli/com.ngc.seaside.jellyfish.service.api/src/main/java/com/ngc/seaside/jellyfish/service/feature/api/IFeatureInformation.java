/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Information about a feature file.
 *
 * @deprecated Use {@link com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature} provided by the
 * {@link com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService} instead.
 */
@Deprecated
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
