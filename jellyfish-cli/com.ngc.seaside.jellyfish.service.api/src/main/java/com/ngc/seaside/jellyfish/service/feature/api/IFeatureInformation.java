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
