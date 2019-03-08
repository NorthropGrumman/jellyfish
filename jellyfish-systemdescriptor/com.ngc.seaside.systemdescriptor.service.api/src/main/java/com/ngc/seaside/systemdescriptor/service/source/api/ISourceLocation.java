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
