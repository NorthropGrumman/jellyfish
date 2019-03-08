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
