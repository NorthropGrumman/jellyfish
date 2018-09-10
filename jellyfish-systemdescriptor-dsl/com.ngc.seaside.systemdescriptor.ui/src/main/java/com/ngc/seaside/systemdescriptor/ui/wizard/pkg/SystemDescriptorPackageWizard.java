/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.ui.wizard.pkg;

import com.ngc.seaside.systemdescriptor.ui.wizard.pkg.page.PackagePage;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;

import java.util.Map;

public class SystemDescriptorPackageWizard extends Wizard implements INewWizard {

   private PackagePage packagePage;
   private IPath defaultSourceFolder;
   private String defaultPackage;

   public SystemDescriptorPackageWizard() {
      super();
      setNeedsProgressMonitor(true);
   }

   @Override
   public void addPages() {
      this.packagePage = new PackagePage(defaultSourceFolder, defaultPackage);
      addPage(this.packagePage);
   }

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection) {
      Map.Entry<IPath, String> result = SystemDescriptorPackageSupport.getDefaultSourceAndPackage(workbench,
                                                                                                  selection);
      this.defaultSourceFolder = result.getKey();
      this.defaultPackage = result.getValue();
   }

   @Override
   public boolean performFinish() {
      IFolder folder = this.packagePage.getAbsolutePath();
      try {
         SystemDescriptorPackageSupport.createFolder(getShell(), folder);
         try {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            for (IViewReference ref : page.getViewReferences()) {
               IViewPart view = ref.getView(false);
               if (view instanceof ISetSelectionTarget) {
                  getShell().getDisplay().asyncExec(() -> {
                     ((ISetSelectionTarget) view).selectReveal(new StructuredSelection(folder));
                  });

               }
            }
         } catch (Exception e) {
            e.printStackTrace(System.err);
         }
      } catch (CoreException e) {
         e.printStackTrace(System.err);
         return false;
      }
      return true;
   }

}
