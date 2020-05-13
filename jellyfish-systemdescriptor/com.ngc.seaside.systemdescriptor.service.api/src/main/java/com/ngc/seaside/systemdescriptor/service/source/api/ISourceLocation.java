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
package com.ngc.seaside.systemdescriptor.service.source.api;

import java.nio.file.Path;

/**
 * Represents the source code location of a System Descriptor element or element within a feature file.
 */
public interface ISourceLocation {

   /**
    * Returns the path to the System Descriptor file or feature file.
    *
    * @return the path to the System Descriptor file or feature file
    */
   Path getPath();

   /**
    * Returns the line number of the location within the {@link #getPath() file}. Line numbers start at {@code 1}.
    *
    * @return the line number of the location
    */
   int getLineNumber();

   /**
    * Returns the character offset from the start of the {@link #getLineNumber() line} where the location starts.
    * Column numbers start at {@code 1}.
    *
    * @return the character offset from the start of the {@link #getLineNumber() line}
    */
   int getColumn();

   /**
    * Returns the character length of the location within the {@link #getPath() file} from the {@link #getColumn()
    * offset}.
    *
    * @return the character length of the location
    */
   int getLength();
}
