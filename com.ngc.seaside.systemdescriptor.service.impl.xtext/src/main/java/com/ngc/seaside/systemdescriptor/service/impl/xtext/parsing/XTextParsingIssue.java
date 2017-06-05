package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;

import org.eclipse.xtext.validation.Issue;

import java.io.File;
import java.nio.file.Path;
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
   public int getLineNumber() {
      return wrapped.getLineNumber();
   }

   @Override
   public int getColumn() {
      return wrapped.getColumn();
   }

   @Override
   public int getOffset() {
      return wrapped.getOffset();
   }

   @Override
   public int getLength() {
      return wrapped.getLength();
   }

   @Override
   public Path getOffendingFile() {
      return new File(wrapped.getUriToProblem().toFileString()).toPath();
   }

   @Override
   public String toString() {
      return String.format("%s: %s [line %s, col %s] - %s",
                           getSeverity(),
                           getOffendingFile(),
                           getLineNumber(),
                           getColumn(),
                           getMessage());
   }

   /**
    * Gets the original wrapped issue.
    */
   public Issue unwrap() {
      return wrapped;
   }
}
