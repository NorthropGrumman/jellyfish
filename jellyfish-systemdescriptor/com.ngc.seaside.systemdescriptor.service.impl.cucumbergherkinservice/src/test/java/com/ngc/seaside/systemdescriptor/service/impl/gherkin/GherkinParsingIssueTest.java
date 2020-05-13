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

import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import gherkin.ParserException;
import gherkin.ast.Location;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GherkinParsingIssueTest {

   @Test
   public void testDoesCreateIssueFromParsing() {
      Path path = Paths.get("some", "path");
      Location location = new Location(1, 2);
      ParserException e = new ParserException("some message", location) {

      };

      GherkinParsingIssue issue = GherkinParsingIssue.forParsingException(path, e);
      assertEquals("message not correct!",
                   e.getMessage(),
                   issue.getMessage());
      assertFalse("errorCode not empty!",
                  issue.getErrorCode().isPresent());
      assertEquals("severity not correct!s",
                   Severity.ERROR,
                   issue.getSeverity());
      assertEquals("path not correct!",
                   path,
                   issue.getLocation().getPath());
      assertEquals("line number not correct!",
                   location.getLine(),
                   issue.getLocation().getLineNumber());
      assertEquals("column not correct!",
                   location.getColumn(),
                   issue.getLocation().getColumn());
      assertEquals("length not correct!",
                   -1,
                   issue.getLocation().getLength());
   }

   @Test
   public void testDoesCreateIssueFromGenericException() {
      Path path = Paths.get("some", "path");
      RuntimeException e = new RuntimeException("some message");

      GherkinParsingIssue issue = GherkinParsingIssue.forException(path, e);
      assertEquals("message not correct!",
                   e.getMessage(),
                   issue.getMessage());
      assertFalse("errorCode not empty!",
                  issue.getErrorCode().isPresent());
      assertEquals("severity not correct!s",
                   Severity.ERROR,
                   issue.getSeverity());
      assertEquals("path not correct!",
                   path,
                   issue.getLocation().getPath());
      assertEquals("line number not correct!",
                   -1,
                   issue.getLocation().getLineNumber());
      assertEquals("column not correct!",
                   -1,
                   issue.getLocation().getColumn());
      assertEquals("length not correct!",
                   -1,
                   issue.getLocation().getLength());
   }
}
