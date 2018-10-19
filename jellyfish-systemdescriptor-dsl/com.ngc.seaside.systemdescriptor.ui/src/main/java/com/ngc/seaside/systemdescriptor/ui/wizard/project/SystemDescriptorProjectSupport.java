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

import com.ngc.seaside.systemdescriptor.ui.wizard.FileHeader;
import com.ngc.seaside.systemdescriptor.ui.wizard.WizardUtils;
import com.ngc.seaside.systemdescriptor.ui.wizard.file.FileWizardUtils;

import org.eclipse.core.internal.resources.ResourceStatus;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Delegate for the creation of the System Descriptor project.
 * The project format is similar to that of a Maven or Gradle project in that
 * the source is located in src/main and the project uses packages similar to that of
 * Java.
 * TODO: Refactor using ITemplateService
 */
public class SystemDescriptorProjectSupport {

   private static final String GRADLE_PROJECT_TEMPLATE_ZIP = "sdproject.zip";

   /**
    * For this project we need to:
    * <pre>
    * - create the default Eclipse project
    * - add the project nature
    * - create the folder structure
    * - create default package and model
    * </pre>
    *
    * @param projectName       The name of the project to create.
    * @param location          Full path of location to create the project.
    * @param gradleProjectName name of the gradle project
    * @param group             gradle project group id
    * @param version           gradle project version
    * @param defaultPkg        The default package to create.
    * @param defaultFile       The default model file to create in the default package.
    * @return Returns the project resource that was created, or null if the creation failed.
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This project already exists in the workspace.</li>
    *                       <li>The name of this resource is not valid (according to
    *                       <code>IWorkspace.validateName</code>).</li>
    *                       <li>The project location is not valid (according to
    *                       <code>IWorkspace.validateProjectLocation</code>).</li>
    *                       <li>The project description file could not be created in the project
    *                       content area.</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       </ul>
    */
   public static IProject createProject(String projectName, URI location, String gradleProjectName, String group,
                                        String version, String defaultPkg, String defaultFile)
         throws CoreException {
      Assert.isNotNull(projectName);
      Assert.isTrue(projectName.trim().length() > 0);

      IProject project = createBaseProject(projectName, location);

      addNature(project, JavaCore.NATURE_ID);

      String mainPathSd = "src/main/sd";
      String mainPathRes = "src/main/resources";
      String testPathGherkin = "src/test/gherkin";
      String testPathRes = "src/test/resources";

      List<String> sourcePathList = new ArrayList<>();
      sourcePathList.add(mainPathSd);
      sourcePathList.add(mainPathRes);
      sourcePathList.add(testPathGherkin);
      sourcePathList.add(testPathRes);

      IJavaProject javaProject = JavaCore.create(project);

      IClasspathEntry[] newEntries = new IClasspathEntry[sourcePathList.size()];
      int index = 0;
      for (String srcFolder : sourcePathList) {
         IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(project.getFolder(srcFolder));
         newEntries[index++] = JavaCore.newSourceEntry(root.getPath());
      }
      javaProject.setRawClasspath(newEntries, null);

      Map<String, String> properties = new LinkedHashMap<>();
      properties.put("projectName", gradleProjectName);
      properties.put("groupId", group);
      properties.put("version", version);
      properties.put("gradleDistributionUrl", WizardUtils.getGradleDistributionUrl());
      FileHeader header = WizardUtils.getFileHeader();
      properties.put("header.properties", header.getProperties());
      properties.put("header.java", header.getJava());
      properties.put("header.gradle", header.getGradle());
      properties.put("header.plain", header.getPlain());
      try {
         addGradleFiles(project, properties);
      } catch (IOException e) {
         throw new CoreException(new ResourceStatus(0,
                                                    project.getFile(GRADLE_PROJECT_TEMPLATE_ZIP)
                                                          .getProjectRelativePath(), e.getMessage(), e));
      }
      addToProjectStructure(project, sourcePathList);

      if (defaultPkg != null) {
         String pkgMainPathSd = mainPathSd + "/" + defaultPkg.replace(".", "/");
         String pkgTestPathGherkin = testPathGherkin + "/" + defaultPkg.replace(".", "/");

         List<String> pkgPathList = new ArrayList<>();
         pkgPathList.add(pkgMainPathSd);
         pkgPathList.add(pkgTestPathGherkin);

         addToProjectStructure(project, pkgPathList);

         if (defaultFile != null) {
            String sd = FileWizardUtils.createDefaultSd(defaultPkg, defaultFile, "model");
            String filepath = pkgMainPathSd + "/" + defaultFile + ".sd";
            addToProject(project, filepath, sd);
         }
      }

      addNature(project, "org.eclipse.xtext.ui.shared.xtextNature");
      addNature(project, "org.eclipse.buildship.core.gradleprojectnature");

      return project;
   }

   /**
    * Create a basic project.
    *
    * @param projectName The name of the project to create.
    * @param location    Full path of location to create the project.
    * @return Returns the project resource that was created.
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This project already exists in the workspace.</li>
    *                       <li>The name of this resource is not valid (according to
    *                       <code>IWorkspace.validateName</code>).</li>
    *                       <li>The project location is not valid (according to
    *                       <code>IWorkspace.validateProjectLocation</code>).</li>
    *                       <li>The project description file could not be created in the project
    *                       content area.</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       </ul>
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
    * @param folder Folder resource to create.
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This resource already exists in the workspace.</li>
    *                       <li>The workspace contains a resource of a different type
    *                       at the same path as this resource.</li>
    *                       <li>The parent of this resource does not exist.</li>
    *                       <li>The parent of this resource is a project that is not open.</li>
    *                       <li>The parent contains a resource of a different type
    *                       at the same path as this resource.</li>
    *                       <li>The parent of this resource is virtual, but this resource is not.</li>
    *                       <li>The name of this resource is not valid (according to
    *                       <code>IWorkspace.validateName</code>).</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a file (as opposed to a directory).</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a folder and <code>force </code> is <code>false</code>.</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       </ul>
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
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This resource already exists in the workspace.</li>
    *                       <li>The parent of this resource does not exist.</li>
    *                       <li>The parent of this resource is a virtual folder.</li>
    *                       <li>The project of this resource is not accessible.</li>
    *                       <li>The parent contains a resource of a different type
    *                       at the same path as this resource.</li>
    *                       <li>The name of this resource is not valid (according to
    *                       <code>IWorkspace.validateName</code>).</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a directory.</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a file and <code>force </code> is <code>false</code>.</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       </ul>
    */
   private static void createFile(IFile file, String content) throws CoreException {
      if (!file.exists()) {

         InputStream contents = new ByteArrayInputStream(content.getBytes());

         IProgressMonitor monitor = new NullProgressMonitor();
         file.create(contents, true, monitor);
      }
   }

   /**
    * Creates a file in a specified project.
    *
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This resource already exists in the workspace.</li>
    *                       <li>The parent of this resource does not exist.</li>
    *                       <li>The parent of this resource is a virtual folder.</li>
    *                       <li>The project of this resource is not accessible.</li>
    *                       <li>The parent contains a resource of a different type
    *                       at the same path as this resource.</li>
    *                       <li>The name of this resource is not valid (according to
    *                       <code>IWorkspace.validateName</code>).</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a directory.</li>
    *                       <li>The corresponding location in the local file system is occupied
    *                       by a file and <code>force </code> is <code>false</code>.</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       </ul>
    */
   private static void createFile(IFile file, InputStream contents) throws CoreException {
      if (!file.exists()) {

         IProgressMonitor monitor = new NullProgressMonitor();
         file.create(contents, true, monitor);
      }
   }

   /**
    * Create a file within the project
    */
   private static void addToProject(IProject project, String filepath, String fileContent) throws CoreException {
      String[] parts = filepath.split("/");
      if (parts.length > 1) {
         IFolder folder = project.getFolder(
               Arrays.asList(parts).subList(0, parts.length - 1).stream().collect(Collectors.joining("/")));
         createFolder(folder);
      }
      IFile file = project.getFile(filepath);
      createFile(file, fileContent);
   }

   /**
    * Create a file within the project
    */
   private static void addToProject(IProject project, String filepath, InputStream fileContent) throws CoreException {
      String[] parts = filepath.split("/");
      if (parts.length > 1) {
         IFolder folder = project.getFolder(
               Arrays.asList(parts).subList(0, parts.length - 1).stream().collect(Collectors.joining("/")));
         createFolder(folder);
      }
      IFile file = project.getFile(filepath);
      createFile(file, fileContent);
   }

   private static void addGradleFiles(IProject project, Map<String, String> properties)
         throws CoreException, IOException {
      try (ZipInputStream stream = new ZipInputStream(
            SystemDescriptorProjectSupport.class.getClassLoader().getResourceAsStream(GRADLE_PROJECT_TEMPLATE_ZIP))) {
         ZipEntry entry;
         byte[] data = new byte[2048];
         while ((entry = stream.getNextEntry()) != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(stream.available());
            int len;
            while ((len = stream.read(data)) > 0) {
               out.write(data, 0, len);
            }
            if (entry.isDirectory()) {
               createFolder(project.getFolder(entry.getName()));
               continue;
            }
            String name = entry.getName();
            if (name.endsWith(".vm")) {
               name = name.substring(0, name.length() - 3);
            }
            InputStream contentStream;
            if (entry.getName().endsWith(".jar")) {
               contentStream = new ByteArrayInputStream(out.toByteArray());
            } else {
               String contents = out.toString();
               if (entry.getName().endsWith(".vm")) {
                  // Remove license comments
                  contents = Pattern.compile("^##.*?$\\v+", Pattern.MULTILINE).matcher(contents).replaceAll("");
               }
               for (Map.Entry<String, String> keyValue : properties.entrySet()) {
                  contents = contents.replace("${" + keyValue.getKey() + "}", keyValue.getValue());
               }
               contentStream = new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
            }
            addToProject(project, name, contentStream);
         }

      }
   }

   /**
    * Create a folder structure within the project
    *
    * @param project  The project resource to create the folder structure in.
    * @param pathList List of the paths of folders to create)}
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
    * @param project The project to add the nature to.
    * @throws CoreException if this method fails. Reasons include:
    *                       <ul>
    *                       <li>This project does not exist in the workspace.</li>
    *                       <li>This project is not open.</li>
    *                       <li>The location in the local file system corresponding to the project
    *                       description file is occupied by a directory.</li>
    *                       <li>The workspace is out of sync with the project description file
    *                       in the local file system .</li>
    *                       <li>Resource changes are disallowed during certain types of resource change
    *                       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
    *                       <li>The file modification validator disallowed the change.</li>
    *                       </ul>
    */
   private static void addNature(IProject project, String nature) throws CoreException {
      IProjectDescription description = project.getDescription();

      String[] prevNatures = description.getNatureIds();
      String[] newNatures = new String[prevNatures.length + 1];

      System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
      newNatures[prevNatures.length] = nature;
      description.setNatureIds(newNatures);

      IProgressMonitor monitor = new NullProgressMonitor();
      project.setDescription(description, monitor);
   }
}
