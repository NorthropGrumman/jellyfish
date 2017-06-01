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
