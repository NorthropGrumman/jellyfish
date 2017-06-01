package com.ngc.seaside.systemdescriptor.ui.wizard.project.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PackagePage extends WizardPage {
	private static final String DEFAULT_PACKAGE = "com.ngc.mydsproject";
	private static final String DEFAULT_FILE = "MyModel";

	private Text pkgTextField;
	private Text filenameTextField;

	private Button createDefaultPkgCheck;
	private Button createDefaultFileCheck;

	private Composite container;

	private KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			updateControls();
		}
	};

	private SelectionListener selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			updateControls();
		}
	};

	public PackagePage() {
		super("Package Info Page");
		setTitle("Package Info Page");
		setDescription("Enter package information");

		setControl(pkgTextField);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		buildPackageInfo();
		buildFileInfo();
		
		// required to avoid an error in the system
		setControl(container);

		updateControls();
	}

	private void buildPackageInfo() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		createDefaultPkgCheck = new Button(container, SWT.CHECK);
		createDefaultPkgCheck.setText("Create default package");
		createDefaultPkgCheck.setSelection(true);
		createDefaultPkgCheck.addSelectionListener(selectionListener);
		new Label(container, SWT.NONE);

		Label labelPkg = new Label(container, SWT.NONE);
		labelPkg.setText("Package name:");

		pkgTextField = new Text(container, SWT.BORDER | SWT.SINGLE);
		pkgTextField.setText(DEFAULT_PACKAGE);
		pkgTextField.setEnabled(true);
		pkgTextField.addKeyListener(keyListener);
		pkgTextField.setLayoutData(gd);

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	private void buildFileInfo() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		createDefaultFileCheck = new Button(container, SWT.CHECK);
		createDefaultFileCheck.setText("Create default file in package");
		createDefaultFileCheck.setSelection(true);
		createDefaultFileCheck.addSelectionListener(selectionListener);
		new Label(container, SWT.NONE);

		Label labelPkg = new Label(container, SWT.NONE);
		labelPkg.setText("File name:");

		filenameTextField = new Text(container, SWT.BORDER | SWT.SINGLE);
		filenameTextField.setText(DEFAULT_FILE);
		filenameTextField.setEnabled(true);
		filenameTextField.addKeyListener(keyListener);
		filenameTextField.setLayoutData(gd);

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	private void updateControls() {
		final boolean createDefaultPkg = createDefaultPkgCheck.getSelection();
		final boolean createDefaultFile = createDefaultFileCheck.getSelection();
		final String pkgText = pkgTextField.getText();
		final String fileText = filenameTextField.getText();

		pkgTextField.setEnabled(createDefaultPkg);
		createDefaultFileCheck.setEnabled(createDefaultPkg);

		filenameTextField.setEnabled(createDefaultFile && createDefaultPkg);

		final boolean complete;
		if (createDefaultPkg) {
			if (pkgText.isEmpty()) {
				complete = false;
			} else if (createDefaultFile) {
				if (fileText.isEmpty()) {
					complete = false;
				} else {
					complete = true;
				}
			} else {
				complete = true;
			}
		} else {
			complete = true;
		}

		setPageComplete(complete);
	}
	
	public boolean getCreatePkg() {
		return createDefaultPkgCheck.getSelection();
	}

	public String getPkg() {
		return pkgTextField.getText();
	}
	
	public boolean getCreateFile() {
		return createDefaultFileCheck.getSelection();
	}

	public String getFileName() {
		return filenameTextField.getText();
	}
}