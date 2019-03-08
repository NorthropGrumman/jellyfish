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
