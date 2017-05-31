package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;

import org.eclipse.xtext.validation.Issue;

import java.io.BufferedReader;
import java.util.Optional;

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
   public BufferedReader getOffendingResouce() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public String toString() {
      return String.format("%s: %s [l%s, c%s] - %s",
                           getSeverity(),
                           null,
                           getLength(),
                           getColumn(),
                           getMessage());
   }
}
