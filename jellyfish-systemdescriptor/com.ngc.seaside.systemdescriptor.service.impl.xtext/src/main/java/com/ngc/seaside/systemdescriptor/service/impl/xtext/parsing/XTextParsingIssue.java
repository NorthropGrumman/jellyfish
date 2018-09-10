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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.XTextSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.eclipse.xtext.validation.Issue;

import java.util.Optional;

/**
 * An implementation of {@code  IParsingIssue} that wraps an XText {@link Issue}.
 */
public class XTextParsingIssue implements IParsingIssue {

   private final Issue wrapped;

   public XTextParsingIssue(Issue wrapped) {
      this.wrapped = wrapped;
   }

   @Override
   public String getMessage() {
      return wrapped.getMessage();
   }

   @Override
   public Severity getSeverity() {
      switch (wrapped.getSeverity()) {
         case ERROR:
            return Severity.ERROR;
         case WARNING:
            return Severity.WARNING;
         default:
            return Severity.SUGGESTION;
      }
   }

   @Override
   public Optional<String> getErrorCode() {
      return Optional.ofNullable(wrapped.getCode());
   }

   @Override
   public ISourceLocation getLocation() {
      return new XTextSourceLocation(wrapped.getUriToProblem(), wrapped.getLineNumber(), wrapped.getColumn(),
               wrapped.getLength());
   }

   @Override
   public String toString() {
      ISourceLocation location = getLocation();
      return String.format("%s: %s [line %s, col %s] - %s",
                           getSeverity(),
                           location.getPath(),
                           location.getLineNumber(),
                           location.getColumn(),
                           getMessage());
   }

   /**
    * Gets the original wrapped issue.
    */
   public Issue unwrap() {
      return wrapped;
   }
}
