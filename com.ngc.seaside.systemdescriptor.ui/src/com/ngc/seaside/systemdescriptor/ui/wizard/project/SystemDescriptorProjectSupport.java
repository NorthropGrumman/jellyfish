package com.ngc.seaside.systemdescriptor.ui.wizard.project;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class SystemDescriptorProjectSupport {
	private static final String NEWLINE = System.getProperty("line.separator");

	/**
	 * For this project we need to: - create the default Eclipse project - add
	 * the custom project nature - create the folder structure
	 *
	 * @param projectName
	 * @param location
	 * @param natureId
	 * @return
	 */
	public static IProject createProject(String projectName, URI location, String defaultPkg, String defaultFile) {
		Assert.isNotNull(projectName);
		Assert.isTrue(projectName.trim().length() > 0);

		IProject project = createBaseProject(projectName, location);

		try {
			addNatures(project);

			String sdPath = "src/main/sd";
			String resPath = "src/main/resources";
			String testPath = "src/test/gherkin";

			List<String> basicPathList = new ArrayList<>();
			basicPathList.add(sdPath);
			basicPathList.add(resPath);
			basicPathList.add(testPath);

			addToProjectStructure(project, basicPathList);

			if (defaultPkg != null) {
				String pkgPath = sdPath + "/" + defaultPkg.replace(".", "/");
				String pkgTestPath = testPath + "/" + defaultPkg.replace(".", "/");

				List<String> pkgPathList = new ArrayList<>();
				pkgPathList.add(pkgPath);
				pkgPathList.add(pkgTestPath);

				addToProjectStructure(project, pkgPathList);

				if (defaultFile != null) {
					StringBuilder sb = new StringBuilder();

					sb.append("package " + defaultPkg).append(NEWLINE);
					sb.append(NEWLINE);
					sb.append("model " + defaultFile + " {").append(NEWLINE);
					sb.append("  metadata {").append(NEWLINE);
					sb.append("    \"name\" : \"" + defaultFile + "\",").append(NEWLINE);
					sb.append("    \"description\" : \"" + defaultFile + " description\",").append(NEWLINE);
					sb.append("    \"stereotypes\" : [\"model\", \"example\"]").append(NEWLINE);
					sb.append("  }").append(NEWLINE);
					sb.append("}").append(NEWLINE);

					String filepath = pkgPath + "/" + defaultFile + ".sd";

					createFile(project, filepath, sb.toString());
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	private static void createFile(IProject project, String filepath, String content) {
		IFile file = project.getFile(filepath);

		if (!file.exists()) {

			InputStream contents = new ByteArrayInputStream(content.getBytes());

			try {
				IProgressMonitor monitor = new NullProgressMonitor();
				file.create(contents, true, monitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Just do the basics: create a basic project.
	 *
	 * @param location
	 * @param projectName
	 */
	private static IProject createBaseProject(String projectName, URI location) {
		// it is acceptable to use the ResourcesPlugin class
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());

			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);

			try {
				newProject.create(desc, null);

				if (!newProject.isOpen()) {
					newProject.open(null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}

	private static void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();

		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}

		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * Create a folder structure
	 *
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */
	private static void addToProjectStructure(IProject newProject, List<String> pathList) throws CoreException {
		for (String path : pathList) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

	private static void addNatures(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();

		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length + 1];

		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);

		newNatures[prevNatures.length] = "org.eclipse.xtext.ui.shared.xtextNature";
		description.setNatureIds(newNatures);

		IProgressMonitor monitor = new NullProgressMonitor();
		project.setDescription(description, monitor);
	}
}
