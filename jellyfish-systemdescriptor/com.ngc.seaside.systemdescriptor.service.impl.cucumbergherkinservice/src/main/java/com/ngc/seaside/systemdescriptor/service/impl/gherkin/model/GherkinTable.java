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
