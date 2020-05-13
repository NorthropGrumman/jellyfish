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
 * Represents a row of data.
 */
public interface IGherkinRow {

   /**
    * Gets the contents of the given cell.  This operation only makes sense if this row is not a {@link
    * IGherkinTable#getHeader() header} row.
    *
    * @param headerName the name of the {@link IGherkinTable#getHeader() header} or column to retrieve
    * @return the contents of the cell or an empty optional if the table contains no header with name {@code headerName}
    */
   Optional<IGherkinCell> getCell(String headerName);

   /**
    * Gets the cells in this row.
    *
    * @return the cells in this row
    */
   List<IGherkinCell> getCells();
}
