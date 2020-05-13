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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin.model;

import java.nio.file.Path;

/**
 * Wraps a Gherkin type.
 * @param <T> the type of Gherkin object being wrapped.
 */
public interface IGherkinUnwrappable<T> {

   /**
    * Returns the original Gherkin object.
    * @return the original Gherkin object
    */
   T unwrap();

   /**
    * Gets the path to the file that contains the source for the object.
    * @return the path to the file that contains the source for the object
    */
   Path getPath();

   /**
    * Gets the line number in the file that contains the source for the object.
    * @return the line number of the source of the object or -1 if no source information is available
    */
   int getLineNumber();

   /**
    * Gets the column in the file that contains the source for the object.
    * @return the column of the source of the object or -1 if no source information is available
    */
   int getColumn();
}
