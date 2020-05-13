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
package com.ngc.seaside.systemdescriptor.ui.wizard.project;

import com.ngc.seaside.systemdescriptor.ui.wizard.project.page.PackageInfoPage;
import com.ngc.seaside.systemdescriptor.ui.wizard.project.page.ProjectInfoPage;

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
   protected ProjectInfoPage gradleProjPage;
   protected PackageInfoPage pkgPage;
   protected StatusManager statusManager;

   /**
    * Constructor.
    */
   public SystemDescriptorProjectWizard() {
      super();
      setNeedsProgressMonitor(true);
   }

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection) {
   }

   @Override
   public String getWindowTitle() {
      return WINDOW_TITLE;
   }

   @Override
   public void addPages() {
      projPage = new WizardNewProjectCreationPage(PAGE_NAME);
      projPage.setTitle(WIZARD_NAME);
      projPage.setDescription(WIZARD_DESC);

      gradleProjPage = new ProjectInfoPage(() -> projPage.getProjectName());
      pkgPage = new PackageInfoPage();

      addPage(projPage);
      addPage(gradleProjPage);
      addPage(pkgPage);
   }

   @Override
   public boolean performFinish() {
      String name = projPage.getProjectName();
      URI location = null;

      if (!projPage.useDefaults()) {
         location = projPage.getLocationURI();
      }

      String projectName = gradleProjPage.getProjectName();
      String group = gradleProjPage.getGroupId();
      String version = gradleProjPage.getVersion();

      String defaultPkg = null;
      if (pkgPage.getCreatePkg()) {
         defaultPkg = pkgPage.getPkg();
      }

      String defaultFile = null;
      if (pkgPage.getCreateFile()) {
         defaultFile = pkgPage.getFileName();
      }

      try {
         SystemDescriptorProjectSupport
               .createProject(name, location, projectName, group, version, defaultPkg, defaultFile);
      } catch (CoreException e) {
         statusManager.handle(
               new Status(IStatus.ERROR, getClass().getName(), "Unable to create project", e));
         throw new RuntimeException(e);
      }

      return true;
   }

}
