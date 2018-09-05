/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.service.api;

import java.nio.file.Path;
import java.util.Collection;

/**
 * A generic exception that may occur during parsing.
 *
 * @see ISystemDescriptorService#parseProject(Path)
 * @see ISystemDescriptorService#parseFiles(Collection)
 */
public class ParsingException extends RuntimeException {

   public ParsingException() {
   }

   public ParsingException(String message) {
      super(message);
   }

   public ParsingException(String message, Throwable cause) {
      super(message, cause);
   }

   public ParsingException(Throwable cause) {
      super(cause);
   }

   public ParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
