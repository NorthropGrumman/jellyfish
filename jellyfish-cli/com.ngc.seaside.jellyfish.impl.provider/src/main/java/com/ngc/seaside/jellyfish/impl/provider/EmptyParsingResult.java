package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public class EmptyParsingResult implements IParsingResult {

   public static final IParsingResult INSTANCE = new EmptyParsingResult();

   private EmptyParsingResult() {
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
      return Collections.emptyList();
   }

   @Override
   public Path getMainSourcesRoot() {
      return null;
   }

   @Override
   public Path getTestSourcesRoot() {
      return null;
   }
}
