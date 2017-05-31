package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.eclipse.xtext.validation.Issue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class XTextParsingResult implements IParsingResult {

   private final Collection<IParsingIssue> issues;
   private final Collection<IParsingIssue> unmodifiableIssues;

   private ISystemDescriptor systemDescriptor;

   public XTextParsingResult() {
      this.issues = new ArrayList<>();
      this.unmodifiableIssues = Collections.unmodifiableCollection(issues);
   }

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return systemDescriptor;
   }

   @Override
   public boolean isSuccessful() {
      return issues.isEmpty();
   }

   @Override
   public Collection<IParsingIssue> getIssues() {
      return unmodifiableIssues;
   }

   XTextParsingResult addIssues(Collection<Issue> xtextIssues) {
      for (Issue i : xtextIssues) {
         issues.add(new XTextParsingIssue(i));
      }
      return this;
   }

   XTextParsingResult setSystemDescriptor(ISystemDescriptor systemDescriptor) {
      this.systemDescriptor = systemDescriptor;
      return this;
   }
}
