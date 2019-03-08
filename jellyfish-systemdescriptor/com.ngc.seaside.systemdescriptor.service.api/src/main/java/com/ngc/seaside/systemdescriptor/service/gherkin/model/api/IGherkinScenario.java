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

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A Gherkin scenario is a specific, executable test case.  Multiple Gherkin scenarios map to a single system descriptor
 * {@link com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario scenario} of a model.
 */
public interface IGherkinScenario extends INamedChild<IFeature> {

   /**
    * Gets the description of this scenario.
    *
    * @return the description of this scenario
    */
   String getDescription();

   /**
    * Gets the tags associated with this scenario.  This does not include any tags in the parent
    * {@link IFeature#getTags feature}.
    *
    * @return the tags associated with this scenario
    */
   Collection<IGherkinTag> getTags();

   /**
    * Returns true if this scenario has the given tag.  This operation will check both the tags of this scenario has
    * the tags of the scenario's parent {@link IFeature#getTags feature} tags.
    *
    * @param tagName the name of the tag which should not start with the "@" symbol
    * @return true if either this scenario has the given tag or its parent feature has the tag
    */
   boolean hasTag(String tagName);

   /**
    * Gets all the steps in this scenario in the order they were declared.
    *
    * @return all the steps in this scenario in the order they were declared
    */
   List<IGherkinStep> getSteps();

   /**
    * Gets the given steps associated with this scenario.
    *
    * @return the given steps associated with this scenario
    */
   List<IGherkinStep> getGivens();

   /**
    * Gets the when steps associated with this scenario.
    *
    * @return the when steps associated with this scenario
    */
   List<IGherkinStep> getWhens();

   /**
    * Gets the then steps associated with this scenario.
    *
    * @return the then steps associated with this scenario
    */
   List<IGherkinStep> getThens();

   /**
    * Returns true if this scenario is declared as an outline.  If this scenario is an outline, then {@link
    * #getExamples() examples} may be set.
    *
    * @return true if this scenario is an outline
    */
   boolean isOutline();

   /**
    * Gets a table of examples provided to this scenario.  This optional is only set if this scenario is an {@link
    * #isOutline() outline}.
    *
    * @return a table of examples provided to this scenario or an empty optional if this scenario is not an outline
    */
   Optional<IGherkinTable> getExamples();

   /**
    * Gets the background scenario of this scenario.  If this scenario has a background, all scenarios declared in the
    * parent feature have the same background.
    *
    * @return the background scenario of this scenario or an empty optional of this scenario has no background
    */
   Optional<IGherkinScenario> getBackground();
}
