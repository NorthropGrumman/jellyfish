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
package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;

/**
 * This interface provides the same information as the {@link ICommandOptions} plus the added System Descriptor model.
 * The commands will be passed this object upon the execution of their tasks by the {@link IJellyFishCommandProvider}.
 *
 * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that
 * the system descriptor is null and it is required, the command should issue an error to the user.</p>
 */
public interface IJellyFishCommandOptions extends ICommandOptions {

   /**
    * Gets the results of parsing the system descriptor project.  This will contain any errors or warnings that were
    * discovered during parsing.
    *
    * @return the results of parsing the system descriptor project (never {@code null})
    */
   IParsingResult getParsingResult();

   /**
    * Gets the results of parsing the feature files associated with the system descriptor project.  This will contain
    * any errors or warnings that where discovered during parsing.
    *
    * @return the results of parsing the feature files (never {@code null})
    */
   IGherkinParsingResult getGherkinParsingResult();

   /**
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the event that
    * the system descriptor is not needed it may be null.
    *
    * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.</p>
    *
    * @return the system descriptor model
    */
   ISystemDescriptor getSystemDescriptor();

}
