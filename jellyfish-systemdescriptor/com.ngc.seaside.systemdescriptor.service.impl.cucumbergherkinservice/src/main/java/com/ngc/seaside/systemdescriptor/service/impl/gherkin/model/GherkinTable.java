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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;

import gherkin.ast.DataTable;
import gherkin.ast.Examples;
import gherkin.ast.Node;
import gherkin.ast.TableRow;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An examples table or table argument.  May contain a header row.
 */
public class GherkinTable extends AbstractWrappedGherkin<Node> implements IGherkinTable {

   private final IGherkinRow headerRow;

   private final List<IGherkinRow> rows;

   private GherkinTable(Node wrapped, Path path, IGherkinRow headerRow, List<IGherkinRow> rows) {
      super(wrapped, path);
      this.headerRow = headerRow;
      this.rows = rows;
   }

   @Override
   public Optional<IGherkinRow> getHeader() {
      return Optional.ofNullable(headerRow);
   }

   @Override
   public List<IGherkinRow> getRows() {
      return Collections.unmodifiableList(rows);
   }

   /**
    * Creates a wrapped table from a table argument.
    *
    * @param dataTable the table
    * @param path      the source file that contains the table
    * @return the wrapped table
    */
   public static GherkinTable from(DataTable dataTable, Path path) {
      return new GherkinTable(dataTable,
                              path,
                              null,
                              dataTable.getRows()
                                    .stream()
                                    .map(r -> new GherkinRow(r, null, path))
                                    .collect(Collectors.toList()));
   }

   /**
    * Creates a wrapped table from an examples outline.
    *
    * @param examples the table
    * @param path     the source file that contains the table
    * @return the wrapped table
    */
   public static GherkinTable from(Examples examples, Path path) {
      TableRow header = examples.getTableHeader();
      return new GherkinTable(examples,
                              path,
                              new GherkinRow(header, null, path),
                              examples.getTableBody()
                                    .stream()
                                    .map(r -> new GherkinRow(r, header, path))
                                    .collect(Collectors.toList()));
   }
}
