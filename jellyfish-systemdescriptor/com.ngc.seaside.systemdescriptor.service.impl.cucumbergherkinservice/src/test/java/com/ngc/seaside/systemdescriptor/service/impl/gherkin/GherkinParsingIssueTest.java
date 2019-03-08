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
