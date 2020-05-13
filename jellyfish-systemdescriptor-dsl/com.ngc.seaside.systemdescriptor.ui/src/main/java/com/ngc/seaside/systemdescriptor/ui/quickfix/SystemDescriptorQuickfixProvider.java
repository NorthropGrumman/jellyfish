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
package com.ngc.seaside.systemdescriptor.ui.quickfix;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.ui.quickfix.imports.ImportQuickfixProvider;
import com.ngc.seaside.systemdescriptor.ui.quickfix.pkg.PackageQuickfixProvider;

import org.eclipse.xtext.ui.editor.quickfix.IssueResolution;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider;
import org.eclipse.xtext.validation.Issue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom quickfixes.
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
public class SystemDescriptorQuickfixProvider implements IssueResolutionProvider {

   @Inject
   private ImportQuickfixProvider importProvider;

   @Inject
   private PackageQuickfixProvider packageProvider;

   /**
    * Gets the issues resolutions providers for imports.
    */
   public Set<IssueResolutionProvider> getProviders() {
      Set<IssueResolutionProvider> providers = new LinkedHashSet<>(Arrays.asList(importProvider, packageProvider));
      providers.remove(null);
      return providers;
   }

   @Override
   public List<IssueResolution> getResolutions(Issue issue) {
      List<IssueResolution> list = new ArrayList<>();
      for (IssueResolutionProvider provider : getProviders()) {
         list.addAll(provider.getResolutions(issue));
      }
      return list;
   }

   @Override
   public boolean hasResolutionFor(String issue) {
      boolean resolution = false;
      for (IssueResolutionProvider provider : getProviders()) {
         resolution |= provider.hasResolutionFor(issue);
      }
      return resolution;
   }

}
