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
