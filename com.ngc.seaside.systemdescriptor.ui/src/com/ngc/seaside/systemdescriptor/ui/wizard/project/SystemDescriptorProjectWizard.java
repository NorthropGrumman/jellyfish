package com.ngc.seaside.systemdescriptor.ui.wizard.project;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.ngc.seaside.systemdescriptor.ui.wizard.project.page.PackagePage;


public class SystemDescriptorProjectWizard extends Wizard implements INewWizard
{
	private static final String PAGE_NAME = "System Descriptor Project Wizard";
	private static final String WIZARD_NAME = "New System Descriptor Project";
	private static final String WIZARD_DESC = "Create a System Descriptor Project.";
	private static final String WINDOW_TITLE = "Sd Window Title";

	protected WizardNewProjectCreationPage projPage;
	protected PackagePage pkgPage;

	public SystemDescriptorProjectWizard()
	{
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
	}

	@Override
	public String getWindowTitle()
	{
		return WINDOW_TITLE;
	}

	@Override
	public void addPages()
	{
		projPage = new WizardNewProjectCreationPage(PAGE_NAME);
		projPage.setTitle(WIZARD_NAME);
		projPage.setDescription(WIZARD_DESC);
		
		pkgPage = new PackagePage();

		addPage(projPage);
		addPage(pkgPage);
	}

	@Override
	public boolean performFinish()
	{
		String name = projPage.getProjectName();
		URI location = null;

		if (!projPage.useDefaults())
		{
			location = projPage.getLocationURI();
		}

		String defaultPkg = null;
		if (pkgPage.getCreatePkg())
		{
			defaultPkg = pkgPage.getPkg();
		}

		String defaultFile = null;
		if (pkgPage.getCreateFile())
		{
			defaultFile = pkgPage.getFileName();
		}

		SystemDescriptorProjectSupport.createProject(name, location, defaultPkg, defaultFile);

		return true;
	}

}
