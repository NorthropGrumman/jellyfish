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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.SourceLocation;
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
      return new SourceLocation(wrapped.getUriToProblem(), wrapped.getLineNumber(), wrapped.getColumn(),
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
