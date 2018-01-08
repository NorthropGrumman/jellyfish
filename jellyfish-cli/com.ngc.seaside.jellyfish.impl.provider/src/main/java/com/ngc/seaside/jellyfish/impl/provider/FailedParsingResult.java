package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class FailedParsingResult implements IParsingResult {

   public static final IParsingResult EMPTY_RESULT = new FailedParsingResult(Collections.emptyList());

   private final Collection<IParsingIssue> issues;

   private FailedParsingResult(Collection<IParsingIssue> issues) {
      this.issues = issues;
   }

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return null;
   }

   @Override
   public boolean isSuccessful() {
      return false;
   }

   @Override
   public Collection<IParsingIssue> getIssues() {
      return issues;
   }

   public static IParsingResult fromException(Throwable t) {
      Preconditions.checkNotNull(t, "t may not be null!");
      return new FailedParsingResult(Collections.singletonList(new ExceptionParsingIssue(t)));
   }

   private static class ExceptionParsingIssue implements IParsingIssue {

      private final Throwable t;

      private ExceptionParsingIssue(Throwable t) {
         this.t = t;
      }

      @Override
      public String getMessage() {
         return t.getMessage();
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
      public int getLineNumber() {
         return -1;
      }

      @Override
      public int getColumn() {
         return -1;
      }

      @Override
      public int getOffset() {
         return -1;
      }

      @Override
      public int getLength() {
         return -1;
      }

      @Override
      public Path getOffendingFile() {
         return null;
      }
   }
}
