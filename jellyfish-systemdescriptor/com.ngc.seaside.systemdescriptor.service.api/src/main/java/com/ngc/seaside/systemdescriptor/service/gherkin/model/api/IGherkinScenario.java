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
