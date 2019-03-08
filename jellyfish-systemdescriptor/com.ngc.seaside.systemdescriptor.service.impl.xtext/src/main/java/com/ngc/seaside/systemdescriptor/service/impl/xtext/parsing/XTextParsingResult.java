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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.eclipse.xtext.validation.Issue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Simple implementation of {@code IParsingResult}.
 */
public class XTextParsingResult implements IParsingResult {

   private final Collection<IParsingIssue> issues;
   private final Collection<IParsingIssue> unmodifiableIssues;
   private Path main;
   private Path test;

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
      return issues.stream().noneMatch(issue -> issue.getSeverity().equals(Severity.ERROR));
   }

   @Override
   public Collection<IParsingIssue> getIssues() {
      return unmodifiableIssues;
   }

   @Override
   public Path getMainSourcesRoot() {
      return main;
   }

   @Override
   public Path getTestSourcesRoot() {
      return test;
   }

   /**
    * Adds issues to this return.
    */
   XTextParsingResult addIssues(Collection<Issue> xtextIssues) {
      for (Issue i : xtextIssues) {
         issues.add(new XTextParsingIssue(i));
      }
      return this;
   }

   /**
    * Sets the system descriptor for this result.
    */
   XTextParsingResult setSystemDescriptor(ISystemDescriptor systemDescriptor) {
      this.systemDescriptor = systemDescriptor;
      return this;
   }

   XTextParsingResult setMainSourcesRoot(Path root) {
      this.main = root;
      return this;
   }

   XTextParsingResult setTestSourcesRoot(Path root) {
      this.test = root;
      return this;
   }
}
