package com.ngc.seaside.systemdescriptor.ui.quickfix;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.ui.quickfix.imports.ImportQuickfixProvider;

import org.eclipse.xtext.ui.editor.quickfix.IssueResolution;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider;
import org.eclipse.xtext.validation.Issue;

import java.util.List;

/**
 * Custom quickfixes.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
public class SystemDescriptorQuickfixProvider implements IssueResolutionProvider {

   @Inject
   private ImportQuickfixProvider provider;

   @Override
   public List<IssueResolution> getResolutions(Issue issue) {
      return provider.getResolutions(issue);
   }

   @Override
   public boolean hasResolutionFor(String issue) {
      return provider.hasResolutionFor(issue);
   }

}
