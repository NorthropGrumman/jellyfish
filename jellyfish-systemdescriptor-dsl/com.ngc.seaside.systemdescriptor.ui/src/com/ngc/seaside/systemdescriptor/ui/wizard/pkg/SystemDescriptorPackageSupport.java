package com.ngc.seaside.systemdescriptor.ui.wizard.pkg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;

public class SystemDescriptorPackageSupport {

	/**
	 * Tries to find the default source folder and package based on the
	 * currently selected file in eclipse and the active file in the editor.
	 * 
	 * @param workbench
	 *            eclipse workbench
	 * @param selection
	 *            selected file in the package explorer
	 * @return the default source folder and package
	 */
	static Map.Entry<IPath, String> getDefaultSourceAndPackage(IWorkbench workbench, IStructuredSelection selection)
	   {
	      try {
	         Object selected = selection.getFirstElement();

	         if (selected instanceof IPackageFragment) {
	            IPackageFragment pkg = (IPackageFragment) selected;
	            return new AbstractMap.SimpleEntry<>(pkg.getParent().getPath(), pkg.getElementName());
	         }
	         if (selected instanceof IPackageFragmentRoot) {
	            IPackageFragmentRoot pkgRoot = (IPackageFragmentRoot) selected;
	            return new AbstractMap.SimpleEntry<>(pkgRoot.getPath(), "");
	         }
	         if (selected instanceof IJavaElement) {
	            selected = ((IJavaElement) selected).getResource();
	         }
	         if (selected instanceof IResource) {
	            return getDefaultSourceAndPackage(((IResource) selected).getProject().getLocation(), ((IResource) selected).getFullPath());
	         }
	         if (selected instanceof ICompilationUnit) {
	            ICompilationUnit unit = (ICompilationUnit) selected;
	            IPackageDeclaration[] pkg = null;
	            try {
	               pkg = unit.getPackageDeclarations();
	               if (pkg.length > 0) {
	                  return new AbstractMap.SimpleEntry<>(pkg[0].getParent().getPath(), pkg[0].getElementName());
	               }
	            }
	            catch (JavaModelException e) {
	            }
	         }

	         // If all else fails look at the project of the active page in eclipse

	         IFile file = ((IFileEditorInput) workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput()).getFile();
	         Map.Entry<IPath, String> result = getDefaultSourceAndPackage(file.getProject().getLocation(), file.getFullPath());
	         return new AbstractMap.SimpleEntry<>(result.getKey(), "");
	      }
	      catch (NullPointerException e) {
	      }
	      catch (Exception e) {
	         e.printStackTrace(System.err);
	      }
	      return new AbstractMap.SimpleEntry<>(new Path(""), "");
	   }

	/**
	 * Searches the path for a src folder and returns the default source folder
	 * and default package as best as possible.
	 * 
	 * @param path
	 *            selected path
	 * @return the default source folder and package
	 */
	private static Map.Entry<IPath, String> getDefaultSourceAndPackage(IPath projectPath, IPath path) {
		IPath defaultSourceFolder = projectPath.append("src").append("main").append("sd");
		if (projectPath.segmentCount() > 0) {
			projectPath = projectPath.removeLastSegments(1);
		}
		if (defaultSourceFolder.toFile().isDirectory()) {
			defaultSourceFolder = defaultSourceFolder.makeRelativeTo(projectPath);
		} else {
			defaultSourceFolder = new Path("");
		}
		while (projectPath.append(path).toFile().isFile() && path.segmentCount() > 0) {
			path = path.removeLastSegments(1);
		}
		List<String> pathList = Arrays.asList(path.segments());

		for (int i = pathList.size() - 1; i >= 0; i--) {
			if (pathList.get(i).equals("src")) {
				if (i + 2 < pathList.size()) {
					IPath sourceFolder = new Path(String.join(File.separator, pathList.subList(0, i + 3)));
					String pkg = String.join(".", pathList.subList(i + 3, pathList.size()));
					return new AbstractMap.SimpleEntry<>(sourceFolder, pkg);
				}
				if (i + 1 < pathList.size()) {
					IPath sourceFolder = new Path(String.join(File.separator, pathList.subList(0, i + 2))).append("sd");
					if (projectPath.append(sourceFolder).toFile().isDirectory()) {
						return new AbstractMap.SimpleEntry<>(sourceFolder, "");
					}
				}
				IPath sourceFolder = new Path(String.join(File.separator, pathList.subList(0, i + 1))).append("main")
						.append("sd");
				if (projectPath.append(sourceFolder).toFile().isDirectory()) {
					return new AbstractMap.SimpleEntry<>(sourceFolder, "");
				}
				break;
			}
		}

		return new AbstractMap.SimpleEntry<>(defaultSourceFolder, "");
	}

	/**
	 * Creates the specified folder.
	 * 
	 * @param folder
	 *            Folder resource to create.
	 * @throws CoreException
	 *             if this method fails. Reasons include:
	 *             <ul>
	 *             <li>This resource already exists in the workspace.</li>
	 *             <li>The workspace contains a resource of a different type at
	 *             the same path as this resource.</li>
	 *             <li>The parent of this resource does not exist.</li>
	 *             <li>The parent of this resource is a project that is not
	 *             open.</li>
	 *             <li>The parent contains a resource of a different type at the
	 *             same path as this resource.</li>
	 *             <li>The parent of this resource is virtual, but this resource
	 *             is not.</li>
	 *             <li>The name of this resource is not valid (according to
	 *             <code>IWorkspace.validateName</code>).</li>
	 *             <li>The corresponding location in the local file system is
	 *             occupied by a file (as opposed to a directory).</li>
	 *             <li>The corresponding location in the local file system is
	 *             occupied by a folder and <code>force </code> is
	 *             <code>false</code>.</li>
	 *             <li>Resource changes are disallowed during certain types of
	 *             resource change event notification. See
	 *             <code>IResourceChangeEvent</code> for more details.</li>
	 *             </ul>
	 */
	static void createFolder(Shell shell, IFolder folder) throws CoreException {
		NullProgressMonitor monitor = new NullProgressMonitor();
		createRecursively(folder, monitor);
	}

	private static void createRecursively(IResource resource, IProgressMonitor monitor) throws CoreException {
		if (resource == null || resource.exists()) {
			return;
		}
		createRecursively(resource.getParent(), monitor);
		switch (resource.getType()) {
		case IResource.FILE:
			((IFile) resource).create(new ByteArrayInputStream(new byte[0]), true, monitor);
			break;
		case IResource.FOLDER:
			((IFolder) resource).create(0, true, monitor);
			break;
		case IResource.PROJECT:
			((IProject) resource).create(monitor);
			((IProject) resource).open(monitor);
			break;
		default:
			return;
		}
	}

}
