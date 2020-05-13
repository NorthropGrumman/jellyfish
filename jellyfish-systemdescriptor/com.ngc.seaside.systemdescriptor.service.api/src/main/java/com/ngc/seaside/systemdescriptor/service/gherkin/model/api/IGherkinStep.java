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

import java.util.List;
import java.util.Optional;

/**
 * A step is a single line in a Gherkin {@link IGherkinScenario scenario}.
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
