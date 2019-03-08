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
 * Represents a table of rows.  {@link IGherkinScenario#getExamples() Example tables} of scenarios usually have headers
 * but tables provided as step {@link IGherkinStep#getTableArgument() arguments} usually do not contain headers.
 */
public interface IGherkinTable {

   /**
    * Gets the header of this table.  In most cases, only tables which are {@link IGherkinScenario#getExamples() example
    * tables} have headers.
    *
    * @return the header of this table or an empty optional if this table has no header
    */
   Optional<IGherkinRow> getHeader();

   /**
    * Gets the rows of this table.
    *
    * @return the rows of this table
    */
   List<IGherkinRow> getRows();
}
