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
