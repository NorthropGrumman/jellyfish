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
