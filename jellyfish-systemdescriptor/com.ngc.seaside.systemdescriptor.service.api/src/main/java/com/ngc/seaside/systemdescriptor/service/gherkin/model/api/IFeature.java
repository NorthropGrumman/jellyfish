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
package com.ngc.seaside.systemdescriptor.service.gherkin.model.api;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Optional;

/**
 * A Gherkin feature is a collection of scenarios.  Gherkin features are associated with a model's {@link IScenario
 * scenario}.
 */
public interface IFeature {

   /**
    * Gets the System Descriptor scenario that is associated with this feature.
    *
    * @return the System Descriptor scenario that is associated with this feature or an empty optional if this feature
    * file is not associated with any known System Descriptor scenario
    */
   Optional<IScenario> getModelScenario();

   /**
    * Gets the name of this feature.  This does not include the package name.  This is typically the name of the feature
    * file itself minus the {@code .feature} extension.
    *
    * @return the name of this feature
    */
   String getName();

   /**
    * Gets the name of the package this feature is contained in.  Subpackages are delimited with ".".
    *
    * @return the name of the package containing this feature
    */
   String getPackage();

   /**
    * Gets a short description of this feature.  This is usually a single sentence.
    *
    * @return a short description of this feature
    */
   String getShortDescription();

   /**
    * Gets a description of this feature.
    *
    * @return a description of this feature
    */
   String getDescription();

   /**
    * Gets the scenarios declared in this feature.
    *
    * @return the scenarios declared in this feature
    */
   INamedChildCollection<IFeature, IGherkinScenario> getScenarios();

   /**
    * Gets the tags associated with this feature.
    *
    * @return the tags associated with this feature
    */
   Collection<IGherkinTag> getTags();

   /**
    * Returns true if this feature has the given tag.
    *
    * @param tagName the name of the tag which should not start with the "@" symbol
    * @return true if this feature has the given tag, false otherwise
    */
   boolean hasTag(String tagName);

   /**
    * Gets the fully qualified name of this feature.  The fully qualified name is the name of the parent package,
    * appended with ".", appended with the name of this feature.  For example, the fully qualified name of a feature
    * named "HelloWorld" which resides in the package named "my.package" would be "my.package.HelloWorld".
    *
    * @return the fully qualified name of this feature
    */
   String getFullyQualifiedName();
}
