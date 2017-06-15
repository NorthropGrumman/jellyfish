package com.ngc.seaside.systemdescriptor.ui.wizard.pkg.page;

import java.io.File;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
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

public class PackagePage extends WizardPage {

	private static final String PAGE_TITLE = "System Descriptor Package";
	private static final String PAGE_DESC = "Create a new System Descriptor Package";

	private Text packageTextField;
	private Text sourceFolderText;
	private Text packageNameText;
	private Text fileText;
	private Button modelButton;
	private Button dataButton;
	private String defaultSourceFolder;
	private String defaultPackage;

	public PackagePage(IPath defaultSourceFolder, String defaultPackage) {
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESC);

		this.defaultSourceFolder = defaultSourceFolder.makeAbsolute().toString();
		if (this.defaultSourceFolder.equals("/")) {
			this.defaultSourceFolder = "";
		}
		this.defaultPackage = defaultPackage;

		setControl(packageTextField);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, 0);
		container.setLayout(new GridLayout(1, false));

		createContainer(container);

		// validate();
		setControl(container);
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
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(container.getShell(),
						ResourcesPlugin.getWorkspace().getRoot(), false,
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
		} else if (defaultPackage.isEmpty()) {
			focus = this.packageNameText;
		} else {
			focus = this.fileText;
		}
		container.getShell().getDisplay().asyncExec(() -> {
			focus.setFocus();
		});

	}

	void validate() {
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
		}

		// File Validation

		String filename = getFileName();

		if (filename.isEmpty()) {
			updateStatus("System descriptor name must be specified");
			return;
		}

		if (filename.toLowerCase().endsWith(".sd")) {
			filename = filename.substring(filename.length() - 3);
		} else if (filename.lastIndexOf('.') != -1) {
			updateStatus("File extension must be \"sd\"");
			return;
		}

		if (!Pattern.matches("[a-zA-Z_]\\w*", filename)) {
			updateStatus("System descriptor name is invalid");
			return;
		}

		// Button Validation
		if (!modelButton.getSelection() && !dataButton.getSelection()) {
			updateStatus("Either Model or Data must be selected");
			return;
		}

		IFile file = getAbsolutePath();
		if (file.exists()) {
			updateStatus("File already exists");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getSourceFolder() {
		return this.sourceFolderText.getText().trim();
	}

	public String getPackageName() {
		return this.packageNameText.getText().trim();
	}

	public String getFileName() {
		return this.fileText.getText().trim();
	}

	public boolean isModelSelected() {
		return modelButton.getSelection();
	}

	public IFile getAbsolutePath() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String filename = getFileName();
		if (!filename.toLowerCase().endsWith(".sd")) {
			filename += ".sd";
		}
		String packageName = getPackageName();
		String fullPath = filename;
		if (packageName != null && !packageName.isEmpty()) {
			fullPath = packageName.replace('.', File.separatorChar) + File.separatorChar + filename;
		}

		Path absolutePath = new Path(getSourceFolder() + File.separatorChar + fullPath);
		return root.getFile(absolutePath);
	}
}
