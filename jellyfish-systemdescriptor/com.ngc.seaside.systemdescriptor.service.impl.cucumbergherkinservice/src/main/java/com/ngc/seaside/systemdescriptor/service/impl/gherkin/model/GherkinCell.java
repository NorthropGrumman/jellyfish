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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinCell;

import gherkin.ast.TableCell;

import java.nio.file.Path;

/**
 * A table cell in an examples table or a table argument.
 */
public class GherkinCell extends AbstractWrappedGherkin<TableCell> implements IGherkinCell {

   /**
    * Creates a wrapped Gherkin cell.
    *
    * @param wrapped the object to wrap
    * @param path    the path to source of the object
    */
   public GherkinCell(TableCell wrapped, Path path) {
      super(wrapped, path);
   }

   @Override
   public String getValue() {
      return wrapped.getValue();
   }
}
