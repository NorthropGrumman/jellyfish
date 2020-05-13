/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
