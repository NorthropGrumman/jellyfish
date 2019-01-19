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
package com.ngc.seaside.systemdescriptor.ui.wizard.pkg.page;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import java.io.File;
import java.util.regex.Pattern;

public class PackagePage extends WizardPage {

   private static final String PAGE_TITLE = "System Descriptor Package";
   private static final String PAGE_DESC = "Create a new System Descriptor Package";

   private Text sourceFolderText;
   private Text packageNameText;
   private String defaultSourceFolder;
   private String defaultPackage;

   /**
    * Creates a new package page.
    */
   public PackagePage(IPath defaultSourceFolder, String defaultPackage) {
      super(PAGE_TITLE);
      setTitle(PAGE_TITLE);
      setDescription(PAGE_DESC);

      this.defaultSourceFolder = defaultSourceFolder.makeAbsolute().toString();
      if (this.defaultSourceFolder.equals("/")) {
         this.defaultSourceFolder = "";
      }
      this.defaultPackage = defaultPackage;
   }

   @Override
   public void createControl(Composite parent) {
      Composite container = new Composite(parent, 0);
      container.setLayout(new GridLayout(1, false));

      createContainer(container);

      validate();
      setControl(container);
   }

   /**
    * Gets the source folder.
    */
   public String getSourceFolder() {
      return this.sourceFolderText.getText().trim();
   }

   /**
    * Gets the name of the package.
    */
   public String getPackageName() {
      return this.packageNameText.getText().trim();
   }

   /**
    * Gets the absolute path to the new package.
    */
   public IFolder getAbsolutePath() {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      String packageName = getPackageName();
      String fullPath = "";
      if (packageName != null && !packageName.isEmpty()) {
         fullPath = packageName.replace('.', File.separatorChar) + File.separatorChar;
      }

      Path absolutePath = new Path(getSourceFolder() + File.separatorChar + fullPath);
      return root.getFolder(absolutePath);
   }

   private void createContainer(Composite parent) {

      final int LABEL_STYLE = SWT.NONE;
      final int TEXT_STYLE = SWT.DRAW_TAB | SWT.BORDER;
      final int GRID_STYLE = SWT.HORIZONTAL | SWT.VERTICAL;
      final int BUTTON_STYLE = SWT.BACKGROUND;

      final Composite container = new Composite(parent, 0);
      container.setLayout(new GridLayout(3, false));
      container.setLayoutData(new GridData(4, 4, true, true));

      // Source folder
      Label sourceLabel = new Label(container, LABEL_STYLE);
      sourceLabel.setText("&Source Folder:");
      this.sourceFolderText = new Text(container, TEXT_STYLE);
      this.sourceFolderText.setText(defaultSourceFolder);
      this.sourceFolderText.addModifyListener(e -> PackagePage.this.validate());
      this.sourceFolderText.setLayoutData(new GridData(GRID_STYLE));
      Button sourceBrowse = new Button(container, BUTTON_STYLE);
      sourceBrowse.setText("Browse...");
      sourceBrowse.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
            ContainerSelectionDialog dialog = new ContainerSelectionDialog(
                  container.getShell(),
                  ResourcesPlugin.getWorkspace().getRoot(),
                  false,
                  "Select System Descriptor source folder (ex: src/main/sd/)");
            dialog.showClosedProjects(false);
            if (dialog.open() == 0) {
               Object[] res = dialog.getResult();
               if (res.length == 1) {
                  sourceFolderText.setText(((Path) res[0]).toString());
               }
            }
         }
      });

      // Package
      Label packageLabel = new Label(container, LABEL_STYLE);
      packageLabel.setText("&Package Name:");
      this.packageNameText = new Text(container, TEXT_STYLE);
      this.packageNameText.setText(defaultPackage);
      this.packageNameText.addModifyListener(e -> PackagePage.this.validate());
      this.packageNameText.setLayoutData(new GridData(GRID_STYLE));
      new Label(container, LABEL_STYLE);

      // Focus
      final Text focus;
      if (defaultSourceFolder.isEmpty()) {
         focus = this.sourceFolderText;
      } else {
         focus = this.packageNameText;
      }
      container.getShell().getDisplay().asyncExec(() -> {
         focus.setFocus();
         focus.setSelection(focus.getText().length());
      });

   }

   private void validate() {
      // Source Folder Validation
      IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(getSourceFolder());

      if (container == null) {
         updateStatus("Source folder is invalid");
         return;
      }

      if (!container.isAccessible()) {
         updateStatus("Project is not writable");
         return;
      }

      IProject project = container.getProject();
      if (project == null) {
         updateStatus("The source folder of an existing project must be specified.");
         return;
      }

      // Package Validation

      String packageName = getPackageName();

      if (packageName.isEmpty()) {
         updateStatus("The package name must be specified");
         return;
      }

      if (!Pattern.matches("[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*", packageName)) {
         updateStatus("Package name is invalid");
         return;
      }

      if (getAbsolutePath().exists()) {
         updateStatus("Package already exists");
         return;
      }

      updateStatus(null);
   }

   private void updateStatus(String message) {
      setErrorMessage(message);
      setPageComplete(message == null);
   }
}
