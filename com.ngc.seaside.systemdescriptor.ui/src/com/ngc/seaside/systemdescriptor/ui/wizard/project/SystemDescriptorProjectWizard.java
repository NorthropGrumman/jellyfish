package com.ngc.seaside.systemdescriptor.ui.wizard.project;

import com.ngc.seaside.systemdescriptor.ui.wizard.project.page.PackagePage;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.statushandlers.StatusManager;

import java.net.URI;

/**
 * This class provides a wizard for inputing the information necessary to create a new
 * System Descriptor project.
 */
public class SystemDescriptorProjectWizard extends Wizard implements INewWizard {
   private static final String PAGE_NAME = "System Descriptor Project Wizard";
   private static final String WIZARD_NAME = "Create a System Descriptor Project";
   private static final String WIZARD_DESC = "Create a System Descriptor Project.";
   private static final String WINDOW_TITLE = "New System Descriptor Project";

   protected WizardNewProjectCreationPage projPage;
   protected PackagePage pkgPage;
   protected StatusManager statusManager;

   /**
    * Constructor.
    */
   public SystemDescriptorProjectWizard() {
      super();
      setNeedsProgressMonitor(true);
   }

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection) {}

   @Override
   public String getWindowTitle() {
      return WINDOW_TITLE;
   }

   @Override
   public void addPages() {
      projPage = new WizardNewProjectCreationPage(PAGE_NAME);
      projPage.setTitle(WIZARD_NAME);
      projPage.setDescription(WIZARD_DESC);

      pkgPage = new PackagePage();

      addPage(projPage);
      addPage(pkgPage);
   }

   @Override
   public boolean performFinish() {
      String name = projPage.getProjectName();
      URI location = null;

      if (!projPage.useDefaults()) {
         location = projPage.getLocationURI();
      }

      String defaultPkg = null;
      if (pkgPage.getCreatePkg()) {
         defaultPkg = pkgPage.getPkg();
      }

      String defaultFile = null;
      if (pkgPage.getCreateFile()) {
         defaultFile = pkgPage.getFileName();
      }

      try {
         SystemDescriptorProjectSupport.createProject(name, location, defaultPkg, defaultFile);
      } catch (CoreException e) {
         statusManager.handle(
            new Status(IStatus.ERROR, getClass().getName(), "Unable to create project", e));
      }

      return true;
   }

}
