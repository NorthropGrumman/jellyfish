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

      String sd = FileWizardUtils.createDefaultSd(packageName, filename, elementType);
      try {
         boolean success = SystemDescriptorFileSupport.createFile(getShell(), file, sd);
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
