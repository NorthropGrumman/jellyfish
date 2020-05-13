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

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;

/**
 * Service for dealing with feature files.
 *
 * @deprecated Use the {@link com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService} instead.  Commands
 * can reference the parsed Gherkin files from {@link IJellyFishCommandOptions#getGherkinParsingResult()}.
 */
@Deprecated
public interface IFeatureService {

   /**
    * Returns all of the feature files associated with the given model.
    *
    * @param options jellyfish command options
    * @param model   the model
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IModel model);

   /**
    * Returns all of the feature files associated with the given scenario.
    *
    * @param options  jellyfish command options
    * @param scenario the scenario to get the features for
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IScenario scenario);

   /**
    * Returns all of the feature files for the system descriptor.
    *
    * @param options jellyfish command options
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getAllFeatures(IJellyFishCommandOptions options);
}
