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
package com.ngc.seaside.systemdescriptor.ui.wizard.file;

import com.ngc.seaside.systemdescriptor.ui.wizard.file.page.FilePage;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;

import java.io.InputStream;
import java.util.Map;

public class SystemDescriptorFileWizard extends Wizard implements INewWizard {

   private FilePage filePage;
   private IPath defaultSourceFolder;
   private String defaultPackage;

   public SystemDescriptorFileWizard() {
      super();
      setNeedsProgressMonitor(true);
   }

   @Override
   public void addPages() {
      this.filePage = new FilePage(defaultSourceFolder, defaultPackage);
      addPage(this.filePage);
   }

   @Override
   public boolean performFinish() {
      IFile file = this.filePage.getAbsolutePath();

      String filename = this.filePage.getFileName();
      String packageName = this.filePage.getPackageName();
      if (filename.toLowerCase().endsWith(".sd")) {
         filename = filename.substring(filename.length() - 3);
      }
      String elementType = this.filePage.getElementType();

      InputStream stream = SystemDescriptorFileSupport.createSDStream(packageName, filename, elementType);
      try {
         boolean success = SystemDescriptorFileSupport.createFile(getShell(), file, stream);
         if (success) {
            getShell().getDisplay().asyncExec(() -> {
               IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
               try {
                  IDE.openEditor(page, file, true);
               } catch (PartInitException localPartInitException) {
                  // TODO TH: Better exception handling.
               }
            });
            try {
               IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
               for (IViewReference ref : page.getViewReferences()) {
                  IViewPart view = ref.getView(false);
                  if (view instanceof ISetSelectionTarget) {
                     getShell().getDisplay().asyncExec(() -> {
                        ((ISetSelectionTarget) view).selectReveal(new StructuredSelection(file));
                     });
                  }
               }
            } catch (Exception e) {
               // TODO TH: FIX THIS.  We don't do this kind of stuff.
               e.printStackTrace(System.err);
            }
         }
         return success;
      } catch (CoreException e) {
         e.printStackTrace();
         return false;
      }
   }

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection) {
      Map.Entry<IPath, String> result = SystemDescriptorFileSupport.getDefaultSourceAndPackage(workbench, selection);
      this.defaultSourceFolder = result.getKey();
      this.defaultPackage = result.getValue();
   }

}
