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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import gherkin.ParserException;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Implementation of {@code IParsingIssue} for Gherkin issues.  Create instances with
 * {@link #forParsingException(Path, ParserException)} and {@link #forException(Path, Throwable)}.
 */
public class GherkinParsingIssue implements IParsingIssue {

   private final Throwable error;
   private final GherkinIssueSourceLocation sourceLocation;

   private GherkinParsingIssue(Throwable error,
                               GherkinIssueSourceLocation sourceLocation) {
      this.error = error;
      this.sourceLocation = sourceLocation;
   }

   @Override
   public String getMessage() {
      return error.getMessage();
   }

   @Override
   public Severity getSeverity() {
      return Severity.ERROR;
   }

   @Override
   public Optional<String> getErrorCode() {
      return Optional.empty();
   }

   @Override
   public ISourceLocation getLocation() {
      return sourceLocation;
   }

   /**
    * Creates a new parsing issue for the given file.
    *
    * @param file the file that contains the issue
    * @param e    the parsing error
    * @return a new issue
    */
   public static GherkinParsingIssue forParsingException(Path file, ParserException e) {
      return new GherkinParsingIssue(e, new GherkinIssueSourceLocation(file,
                                                                       e.location == null ? -1 : e.location.getLine(),
                                                                       e.location == null ? -1
                                                                                          : e.location.getColumn()));
   }

   /**
    * Creates a new parsing issue for the given file.
    *
    * @param file the file that contains the issue
    * @param e    the error
    * @return a new issue
    */
   public static GherkinParsingIssue forException(Path file, Throwable e) {
      return new GherkinParsingIssue(e, new GherkinIssueSourceLocation(file, -1, -1));
   }

   private static class GherkinIssueSourceLocation implements ISourceLocation {

      final Path path;
      final int lineNumber;
      final int column;

      GherkinIssueSourceLocation(Path path, int lineNumber, int column) {
         this.path = path;
         this.lineNumber = lineNumber;
         this.column = column;
      }

      @Override
      public Path getPath() {
         return path;
      }

      @Override
      public int getLineNumber() {
         return lineNumber;
      }

      @Override
      public int getColumn() {
         return column;
      }

      @Override
      public int getLength() {
         return -1;
      }
   }
}
