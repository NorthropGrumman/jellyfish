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
package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Stores the result of attempting to parse system descriptor files.
 *
 * @see ISystemDescriptorService#parseProject(Path)
 * @see ISystemDescriptorService#parseFiles(Collection)
 */
public interface IParsingResult {

   /**
    * Gets the system descriptor which contains all the packages that were successfully parsed. Use {@link
    * #isSuccessful()} to verify if the result of parsing was successful.
    */
   ISystemDescriptor getSystemDescriptor();

   /**
    * Returns true if the system descriptor files were successfully parsed without errors. If false is returned, the
    * {@code ISystemDescriptor} associated with this result may not be valid or may only be partially complete.
    *
    * @return true if the system descriptor files were successfully parsed without errors, false if there are errors
    */
   boolean isSuccessful();

   /**
    * Gets all issues associated with the parsed system descriptor. If there are no issues, an empty collection is
    * returned.
    *
    * @return an unmodifiable collection of parsing related issues
    */
   Collection<IParsingIssue> getIssues();

   /**
    * Returns a path to the root directory containing the main source files in the system descriptor project. This
    * includes system descriptor files and resource files. This method may return {@code null}.
    * 
    * @return the root directory containing the main source files
    */
   Path getMainSourcesRoot();

   /**
    * Returns a path to the root directory containing the test source files in the system descriptor project. This
    * includes feature files and test resource files. This method may return {@code null}.
    * 
    * @return the root directory containing the test source files
    */
   Path getTestSourcesRoot();

}
