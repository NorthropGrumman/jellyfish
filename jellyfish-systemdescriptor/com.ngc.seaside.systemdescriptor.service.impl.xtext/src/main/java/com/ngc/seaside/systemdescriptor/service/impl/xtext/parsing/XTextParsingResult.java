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
