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

import java.util.List;
import java.util.Optional;

/**
 * A step is a single line in a Gherkin {@link IGherkinScenario scenaio}.
 */
public interface IGherkinStep {

   /**
    * Gets the scenario this step is a part of.
    *
    * @return the scenario this step is a part of
    */
   IGherkinScenario getParent();

   /**
    * Gets the keyword of this step.
    *
    * @return the keyword of this step
    */
   GherkinStepKeyword getKeyword();

   /**
    * Gets the textual content of this step.  This does not include the step's keyword.
    *
    * @return the textual content of this step
    */
   String getContent();

   /**
    * Gets the table of this step provided this step has a table argument.  These types of tables do not have {@link
    * IGherkinTable#getHeader() header rows}.  If this step has a table argument, it cannot have a {@link
    * #getDocStringArgument() doc-string argument}.
    *
    * @return the table of this step or an empty optional of this scenario has no table argument
    */
   Optional<IGherkinTable> getTableArgument();

   /**
    * Gets the doc-string argument of this step provided this step has a doc-string argument.  A doc-string argument is
    * a string declared within triple double quotes (ie, {@code """ hello world """"}).  Such strings can span multiple
    * lines.  The returned list contains each line in the string with line breaks removed.  If this scenario has a
    * doc-string argument it cannot have a {@link #getTableArgument() table argument}.
    *
    * @return the doc-string argument of this step or an empty list if this step has no doc-string argument
    */
   List<String> getDocStringArgument();
}
