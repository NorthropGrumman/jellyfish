package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;

import java.io.BufferedReader;
import java.util.Optional;

public class XTextParsingIssue implements IParsingIssue {

   @Override
   public String getMessage() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Severity getSeverity() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Optional<String> getErrorCode() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public int getLineNumber() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public int getColumn() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public int getOffset() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public int getLength() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public BufferedReader getOffendingResouce() {
      throw new UnsupportedOperationException("not implemented");
   }
}
