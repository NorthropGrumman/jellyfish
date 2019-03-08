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
