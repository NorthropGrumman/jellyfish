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
