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

/**
 * 
 *
 */
public class SystemDescriptorProjectSupport {
	private static final String NEWLINE = System.getProperty("line.separator");

	/**
	 * For this project we need to: 
	 * - create the default Eclipse project 
	 * - add the project nature 
	 * - create the folder structure
	 * - create default package and model
	 *
	 * @param projectName
	 * 		The name of the project to create.
	 * @param location
	 * 		Full path of location to create the project.
	 * @param defaultPkg
	 * 		The default package to create.
	 * @param defaultFile
	 * 		The default model file to create in the default package.
	 * 
	 * @return
	 * 		Returns the project resource that was created, or null if the creation failed.
	 */
	public static IProject createProject(String projectName, URI location, String defaultPkg, String defaultFile) {
		Assert.isNotNull(projectName);
		Assert.isTrue(projectName.trim().length() > 0);

		IProject project = null;
		
		try {
			project = createBaseProject(projectName, location);

			addNature(project);

			String sdPath   = "src/main/sd";
			String resPath  = "src/main/resources";
			String testPath = "src/test/gherkin";

			List<String> basicPathList = new ArrayList<>();
			basicPathList.add(sdPath);
			basicPathList.add(resPath);
			basicPathList.add(testPath);

			addToProjectStructure(project, basicPathList);

			if (defaultPkg != null) {
				String pkgPath     = sdPath   + "/" + defaultPkg.replace(".", "/");
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
					String fileContent = sb.toString();
					
					addToProject(project, filepath, fileContent);
				}
			}
		} catch (CoreException e) {			
			// TODO What happens if we created the project and then have an exception
		    //      (i.e. filename has invalid characters)... Do we want to delete the
			//		partially created project? Popup instead of printstacktrace?
			
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	/**
	 * Create a basic project.
	 *
	 * @param projectName
	 * 		The name of the project to create.
	 * @param location
	 * 		Full path of location to create the project.
	 * 
	 * @return
	 * 		Returns the project resource that was created.
	 * 
	 * @throws CoreException 
	 */
	private static IProject createBaseProject(String projectName, URI location) throws CoreException {
		// it is acceptable to use the ResourcesPlugin class
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!project.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());

			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);

			project.create(desc, null);

			if (!project.isOpen()) {
				project.open(null);
			}
		}

		return project;
	}

	/**
	 * Creates the specified folder.
	 * 
	 * @param folder
	 * 		Folder resource to create.
	 * 
	 * @throws CoreException
	 */
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
	 * Creates a file in a specified project. 
	 *  
	 * @param project
	 * 		The project to put the file in.
	 * @param filepath
	 * 		The full path of the file in the project to create.
	 * @param content
	 * 		The content to write into the file.
	 * 
	 * @throws CoreException 
	 */
	private static void createFile(IFile file, String content) throws CoreException {
		if (!file.exists()) {

			InputStream contents = new ByteArrayInputStream(content.getBytes());

			IProgressMonitor monitor = new NullProgressMonitor();
			file.create(contents, true, monitor);
		}
	}

	/**
	 * Create a file within the project
	 *
	 * @param project
	 * 		The project resource to create the file in. 
	 * @param filepath
	 * 		The full path of the file in the project to create.
	 * @param content
	 * 		The content to write into the file.
	 * 
	 * @throws CoreException
	 */
	private static void addToProject(IProject project, String filepath, String fileContent) throws CoreException {
		IFile file = project.getFile(filepath);
		
		createFile(file, fileContent);
	}

	/**
	 * Create a folder structure within the project
	 *
	 * @param project
	 * 		The project resource to create the folder structure in. 
	 * @param pathList
	 * 		List of the paths of folders to create
	 * 
	 * @throws CoreException
	 */
	private static void addToProjectStructure(IProject project, List<String> pathList) throws CoreException {
		for (String path : pathList) {
			IFolder etcFolders = project.getFolder(path);
			createFolder(etcFolders);
		}
	}

	/**
	 * Add the xtext nature to the project.
	 * 
	 * @param project
	 * 		The project to add the nature to.
	 * 
	 * @throws CoreException
	 */
	private static void addNature(IProject project) throws CoreException {
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
