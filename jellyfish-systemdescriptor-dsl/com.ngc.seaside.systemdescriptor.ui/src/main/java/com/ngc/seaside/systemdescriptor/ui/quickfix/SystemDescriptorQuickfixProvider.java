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
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
public class SystemDescriptorQuickfixProvider implements IssueResolutionProvider {

   @Inject
   private ImportQuickfixProvider importProvider;
   
   @Inject
   private PackageQuickfixProvider packageProvider;
   
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
