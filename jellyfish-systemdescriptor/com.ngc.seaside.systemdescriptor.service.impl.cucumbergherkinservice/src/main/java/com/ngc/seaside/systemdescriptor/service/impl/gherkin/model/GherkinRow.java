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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinCell;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;

import gherkin.ast.TableRow;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A row in an examples table or a table argument.
 */
public class GherkinRow extends AbstractWrappedGherkin<TableRow> implements IGherkinRow {

   private final TableRow headerRow;

   private final List<IGherkinCell> cells;

   /**
    * Creates a new wrapped table.
    *
    * @param wrapped   the object to wrap
    * @param headerRow the optional header row of the table
    * @param path      the path to source of the object
    */
   public GherkinRow(TableRow wrapped, TableRow headerRow, Path path) {
      super(wrapped, path);
      this.headerRow = headerRow;
      cells = wrapped.getCells()
            .stream()
            .map(c -> new GherkinCell(c, path))
            .collect(Collectors.toList());
   }

   @Override
   public Optional<IGherkinCell> getCell(String headerName) {
      Preconditions.checkNotNull(headerName, "headerName may not be null!");
      Preconditions.checkArgument(!headerName.trim().isEmpty(), "headerName may not be empty!");

      Optional<IGherkinCell> cell = Optional.empty();
      if (headerRow != null) {
         for (int i = 0; i < headerRow.getCells().size() && !cell.isPresent(); i++) {
            if (headerRow.getCells().get(i).getValue().equals(headerName)) {
               cell = Optional.of(cells.get(i));
            }
         }
      }
      return cell;
   }

   @Override
   public List<IGherkinCell> getCells() {
      return Collections.unmodifiableList(cells);
   }
}
