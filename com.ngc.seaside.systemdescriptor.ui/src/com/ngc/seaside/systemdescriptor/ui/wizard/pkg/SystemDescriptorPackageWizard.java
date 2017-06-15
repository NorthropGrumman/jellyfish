package com.ngc.seaside.systemdescriptor.ui.wizard.pkg;

import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.ngc.seaside.systemdescriptor.ui.wizard.pkg.page.PackagePage;

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
		Map.Entry<IPath, String> result = SystemDescriptorPackageSupport.getDefaultSourceAndPackage(workbench, selection);
		this.defaultSourceFolder = result.getKey();
		this.defaultPackage = result.getValue();
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
