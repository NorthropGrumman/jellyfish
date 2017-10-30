package com.ngc.seaside.jellyfish.tests;

import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;

import org.apache.commons.io.FilenameUtils;
import org.gradle.api.logging.Logger;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.fail;

public class RegressionsIT {

   private static final String[] SYSTEM_DESCRIPTOR_PROJECTS = {
         "threat-eval-system-descriptor",
         "data-inheritance-descriptor",
         "pubsub-system-descriptor",
         "qualified-names-system-descriptor"
   };

   private static final String[] IGNORED_FILE_NAMES = {
         ".*\\.bin$",
         ".*\\.jar$",
         ".*\\.lock$",
         ".*\\.class$",
         ".*\\.tar$",
         ".*\\.tar\\.gz$",
         ".*\\.tgz",
         ".*\\.html$",
         ".*\\.zip$",
         ".*javadoc.*"
   };

   // Class variable to hold regressions directory
   private String rootDir = "";
   private String regressionTestsDir = "";
   private String jellyFishVersion = VersionUtil.getJellyfishVersion();
   private BuildScriptUpdater scriptUpdater;

   @Before
   public void setup() throws Throwable {
      rootDir = System.getProperty("user.dir");
      // Set the regressions directory variable
      regressionTestsDir = rootDir + File.separator + "regressions";

      scriptUpdater = new BuildScriptUpdater();

      // Start with fresh logs. Remove any previously generated 'build' folders in the 
      //    given projects
      removeGivenProjectsBuildFolder(regressionTestsDir);

      // Start with fresh logs. Remove any previously generated projects
      removeGeneratedProjects(regressionTestsDir);
   }

   /**
    * This is the start method for the regression test
    *
    * @throws IOException - Looking through directories and File reading
    */
   @Test
   public void testGenerationAndDiff() throws IOException {
      buildAndInstallSdProjects();

      File[] subs = new File(regressionTestsDir).listFiles();
      Map<String, Boolean> regressionScore = new HashMap<>();

      if (subs == null || subs.length == 0) {
         fail("Error: There are no directories under the 'regressions' folder. This test"
              + "looks for projects under the 'regressions' folder to perform tests.");
      } else {

         // Loop through the subdirectories under 'regressions' and perform operations
         for (File subDir : subs) {
            if (subDir.isDirectory()) {

               // Call to method that runs jellyfish, and record the return value 
               regressionScore.put(subDir.getName(), generateJellyfishProject(subDir));
            }
         }

         printRegressionSummary(regressionScore);
      }

      if (!regressionScore.containsValue(false)) {
         // All the tests pass. No need to keep the generated files
         removeGeneratedProjects(regressionTestsDir);
      }

      // The below Assert will check to see if there are any failures in the diff evaluation
      Assert.assertFalse(regressionScore.containsValue(false));
   }

   @After
   public void cleanUp() throws Throwable {
      scriptUpdater.restoreAllScripts();
   }

   private void buildAndInstallSdProjects() throws IOException {
      File gradleInstall = getGradleInstallPath();

      File mainDir = new File(System.getProperty("user.dir"));
      for (String sdProject : SYSTEM_DESCRIPTOR_PROJECTS) {
         File projectDir = new File(mainDir, sdProject);
         System.out.println("Running 'gradle clean build install' on SD project: " + sdProject);

         scriptUpdater.updateJellyFishGradlePluginsVersion(new File(projectDir, "build.gradle").toPath(),
                                                           jellyFishVersion);

         ProjectConnection connection = GradleConnector.newConnector()
               .useInstallation(gradleInstall)
               .forProjectDirectory(projectDir)
               .connect();
         try (OutputStream out = Files.newOutputStream(Paths.get("build", "gradle-" + sdProject + ".log"))) {
            connection.newBuild()
                  .forTasks("clean", "build", "install")
                  .setStandardOutput(out)
                  .setStandardError(out)
                  .run();
         } finally {
            connection.close();
         }
      }
   }

   private File getGradleInstallPath() throws IOException {
      File gradle = null;
      String gradleHome = System.getenv("GRADLE_HOME");
      String gradleUserHome = System.getenv("GRADLE_USER_HOME");
      if (gradleHome != null) {
         gradle = Paths.get(gradleHome).toFile();
      } else if (gradleUserHome != null) {
         Properties wrapperProps = new Properties();
         try (InputStream is = Files.newInputStream(Paths.get(rootDir,
                                                              "gradle",
                                                              "wrapper",
                                                              "gradle-wrapper.properties"))) {
            wrapperProps.load(is);
         }

         String version = wrapperProps.getProperty("distributionUrl");
         version = FilenameUtils.removeExtension(version.substring(version.lastIndexOf('/')));
         gradle = Paths.get(gradleUserHome,
                            wrapperProps.getProperty("distributionPath"),
                            version)
               .toFile();
         gradle = new File(gradle.listFiles()[0], version.replace("-bin", ""));
      }

      Assert.assertNotNull(
            "unable find a gradle installation; set the env variable GRADLE_HOME to a gradle installation directory",
            gradle);
      Assert.assertTrue(
            "unable find a gradle installation; set the env variable GRADLE_HOME to a gradle installation directory",
            gradle.isDirectory());
      return gradle;
   }

   /**
    * This method is called to remove 'generatedProject' folders in each regression test
    *
    * @param regDir - the 'regressions' directory
    */
   private static void removeGeneratedProjects(String regDir) {
      File[] subs = new File(regDir).listFiles();

      if (subs == null || subs.length == 0) {
         fail("Error: There are no directories under the 'regressions' folder. This test"
              + "looks for projects under the 'regressions' folder to perform tests.");
      } else {

         // Loop through the subdirectories under 'regressions' and delete any 'generatedProject' folders
         for (File subDir : subs) {
            if (subDir.isDirectory()) {
               File generatedProj = new File(subDir.getAbsoluteFile() + File.separator + "generatedProject");
               System.out.println("Cleaning up directory: " + generatedProj);
               if (generatedProj.exists()) {
                  deleteDir(generatedProj);
               }
            }
         }
      }
   }

   /**
    * This method is called to remove 'build' folders in the given project of each regression test
    *
    * @param regDir - the 'regressions' directory
    */
   private static void removeGivenProjectsBuildFolder(String regDir) {
      File[] subs = new File(regDir).listFiles();

      if (subs == null || subs.length == 0) {
         fail("Error: There are no directories under the 'regressions' folder. This test"
              + "looks for projects under the 'regressions' folder to perform tests.");
      } else {

         // Loop through the subdirectories under 'regressions' and delete any 'build' folders under the 
         //    provided projects
         for (File regressionTestFolder : subs) {
            if (regressionTestFolder.isDirectory()) {

               // regressionTestFolder is regressions\1\, regressions\2\, etc.
               for (File regressionTestSubFolder : regressionTestFolder.listFiles()) {

                  // find the given project
                  if (regressionTestSubFolder.getName().matches("^com.ngc.*$")) {
                     removeBuildFolders(regressionTestSubFolder);
                  }
               }
            }
         }
      }
   }

   /**
    * Recursive method to search for folders named 'build' and delete them.
    *
    * @param regressionTestSubFolder - the current folder
    */
   private static void removeBuildFolders(File regressionTestSubFolder) {
      for (File subFile : regressionTestSubFolder.listFiles()) {
         if (subFile.getName().equals("build")) {
            System.out.println("Cleaning up directory: " + subFile.getAbsolutePath());
            deleteDir(subFile);
         } else if (subFile.isDirectory()) {
            removeBuildFolders(subFile);
         }
      }

   }

   /**
    * Simple method to display an organized summary at the end of the build.
    */
   private static void printRegressionSummary(Map<String, Boolean> regressionScore) {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\n+---------------------------------------");
      sb.append("\n| REGRESSION TEST: ");

      if (regressionScore.containsValue(false)) { //if any regression test failed
         sb.append("FAILED");
      } else {
         sb.append("PASSED");
      }

      sb.append("\n+------------------------\n|\n");
      sb.append("|\tRegression Test:\tSuccess:\n");

      for (String regNum : regressionScore.keySet()) {
         sb.append("|\t" + regNum + "\t\t\t" + regressionScore.get(regNum) + "\n");
      }

      sb.append("|\n+---------------------------------------\n");
      System.out.println(sb.toString());
   }

   /**
    * Run Jellyfish using .properties file information
    *
    * @param directory - the directory of the regression test (ex: 1, 2, etc)
    */
   private boolean generateJellyfishProject(File directory) throws IOException {
      System.out.println("Generating jelly fish project in directory: " + directory);

      // Parse the properties file and store the relevant data
      Map<String, String> propertiesValues = readJellyfishPropertiesFile(directory.getAbsolutePath());

      Map<String, String> generatorArguments = new HashMap<>(propertiesValues);
      // Set these default properties.
      generatorArguments.put("outputDirectory", directory.getAbsolutePath());
      generatorArguments.put("projectName", "generatedProject");
      generatorArguments.put("jellyfishGradlePluginsVersion", jellyFishVersion);

      // Create the generation project object

      Logger log = Mockito.mock(Logger.class);
      JellyFishProjectGenerator proj = new JellyFishProjectGenerator(log)
            .setCommand(propertiesValues.get("command"))
            .setArguments(generatorArguments);

      if (propertiesValues.containsKey("inputDir")) {
         String relPath = File.separator + propertiesValues.get("inputDir").replace('/', File.separatorChar);
         proj.setInputDir(directory + relPath);
      }

      // Generate the object with a firey passion
      proj.generate();

      // At this point, the project has been generated. Now, run 'gradle clean build -x text' on each subproject
      boolean pass = runGradleCleanBuildOnProjects(directory.getAbsolutePath());
      return pass;
   }


   /**
    * Run 'gradle clean build -x test' command for the generated subproject
    *
    * @param directory - the directory of the newly generated regression test project
    */
   private boolean runGradleCleanBuildOnProjects(String directory) throws IOException {

      String givenProject = "";
      for (File file : new File(directory).listFiles()) {
         if (file.getName().contains("com.ngc")) {
            givenProject = file.getAbsolutePath();
            break;
         }
      }

      Assert.assertTrue(givenProject != "");
      String generatedProj = directory + File.separator + "generatedProject";

      File gradleInstall = getGradleInstallPath();

      // Perform the 'gradle clean build -x test' command
      // NOTE: The below build fails on Windows due to the "windows file path too long" bug. 

      System.out.println("Running 'gradle clean build -x test' on given project: " + givenProject);
      ProjectConnection connectionToGivenProj = GradleConnector.newConnector()
            .useInstallation(gradleInstall)
            .forProjectDirectory(new File(givenProject))
            .connect();

      try (OutputStream log = Files.newOutputStream(Paths.get(directory, "gradle.actual.log"))) {
         scriptUpdater.updateJellyFishGradlePluginsVersion(Paths.get(givenProject, "build.gradle"),
                                                           jellyFishVersion);

         BuildLauncher build = connectionToGivenProj.newBuild();
         build.forTasks("clean", "build");
         build.withArguments("-x", "test");
         build.setStandardError(log).
               setStandardOutput(log);

         build.run();
      } finally {
         connectionToGivenProj.close();
      }

      System.out.println("Running 'gradle clean build -x test' on newly generated project: " + generatedProj);
      ProjectConnection connectionToGeneratedProj = GradleConnector.newConnector()
            .useInstallation(gradleInstall)
            .forProjectDirectory(new File(generatedProj))
            .connect();

      try (OutputStream log = Files.newOutputStream(Paths.get(directory, "gradle.generated.log"))) {
         BuildLauncher build = connectionToGeneratedProj.newBuild();
         build.forTasks("clean", "build");
         build.withArguments("-x", "test");
         build.setStandardError(log).setStandardOutput(log);

         build.run();
      } finally {
         connectionToGeneratedProj.close();
      }

      boolean passDiff = diffDefaultAndGeneratedProjectTrees(generatedProj);
      return passDiff;
   }

   /**
    * Perform a diff on the generated project and the given project. They should match
    */
   private static boolean diffDefaultAndGeneratedProjectTrees(String generated) throws IOException {
      String base = generated.replace(File.separator + "generatedProject", "");
      Assert.assertTrue("Base folder doesn't exist: " + base, new File(base).exists());

      File defaultProj = null;
      for (File file : new File(base).listFiles()) {
         if (file.getName().contains("com.ngc")) {
            defaultProj = file;
            break;
         }
      }

      Assert.assertNotNull("Error - Missing 'com.ngc...' subdirectory under " + base,
                           defaultProj);

      Assert.assertTrue("Generated folder doesn't exist: " + generated, new File(generated).exists());
      Assert.assertTrue("Given folder doesn't exist: " + defaultProj, defaultProj.exists());
      File genDir = new File(generated);

      System.out.println("\nComparing Project Trees:\n------------------------");
      System.out.println("Default Project  : " + defaultProj);
      System.out.println("Generated Project: " + generated + "\n");

      boolean pass = compareDirectories(defaultProj, genDir);
      return pass;

   }

   /**
    * Recursively search through the project tree, comparing directories and files along the way. Return a false value
    * if a mismatch is found.
    *
    * @param defaultProj the File corresponding to the provided project
    * @param genProj     the File corresponding to the project that was just generated in generateJellyfishProject()
    * @return a boolean value indicating that the directory tree is equal
    * @throws IOException Working with FileUtils library
    */
   private static boolean compareDirectories(File defaultProj, File genProj) throws IOException {

      boolean sizeEquality = true;
      boolean typeEquality = true;
      boolean fileEquality = true;
      boolean dirEquality = true;

      ArrayList<File> defaultFiles = new ArrayList<File>();
      ArrayList<File> genProjFiles = new ArrayList<File>();
      for (File file : defaultProj.listFiles()) {
         defaultFiles.add(file);
      }

      for (File file : genProj.listFiles()) {
         genProjFiles.add(file);
      }
      defaultFiles.sort(null);
      genProjFiles.sort(null);

      // The trees must contain the same number of elements to be equal
      if (defaultFiles.size() != genProjFiles.size()) {
         prettyPrintFolderContainDifferentListFiles(defaultProj, genProj);
         sizeEquality = false;
      } else {
         // Loop through the elements and evaluate them in each project
         for (int i = 0; i < defaultFiles.size(); i++) {
            File subFileDefDir = defaultFiles.get(i);
            File subFileGenDir = genProjFiles.get(i);

            if (subFileDefDir.isFile() && subFileGenDir.isDirectory() ||
                subFileDefDir.isDirectory() && subFileGenDir.isFile()) {
               prettyPrintFilesDifferentType(subFileDefDir, subFileGenDir);
               typeEquality = false;
            }

            if (subFileDefDir.isFile() && subFileGenDir.isFile()) {
               if (!FileUtils.contentEquals(subFileDefDir, subFileGenDir)) { // This could throw an IO Exception
                  fileEquality = validFileDifferences(subFileDefDir, subFileGenDir);
               }
            }

            if (subFileDefDir.isDirectory() && subFileGenDir.isDirectory()) {
               dirEquality = compareDirectories(subFileDefDir, subFileGenDir);
            }

            if (!(typeEquality && fileEquality && dirEquality)) {
               break;
            }
         }
      }

      return sizeEquality && typeEquality && fileEquality && dirEquality;
   }

   /**
    * Method to display a user friendly message indicating that the directory arguments do not contain the same number
    * of elements
    *
    * @param defProj - the given project
    * @param genProj - the generated project
    */
   private static void prettyPrintFolderContainDifferentListFiles(File defProj, File genProj) {
      System.out.println("--------------------------\nDirectories are not equal:");

      File[] subDir1 = defProj.listFiles();
      File[] subDir2 = genProj.listFiles();

      System.out.println("Folder: " + defProj.getAbsolutePath());
      for (int i = 0; i < subDir1.length; i++) {
         System.out.println("\t- " + subDir1[i].getName());
      }

      System.out.println("\nFolder: " + genProj.getAbsolutePath());
      for (int i = 0; i < subDir2.length; i++) {
         System.out.println("\t- " + subDir2[i].getName());
      }

      System.out.println("");
   }

   /**
    * This method is called to check that unequal files are indeed "different". Differences in files with certain file
    * types and on lines in comments can be ignored.
    *
    * @param defFile - the given project
    * @param genFile - the generated project
    * @return a boolean value indicating whether or not the files are indeed equal (contain no valid differences)
    * @throws IOException working with the FileReader library.
    */
   private static boolean validFileDifferences(File defFile, File genFile) throws IOException {
      boolean equalFiles = true;

      for (String pattern : IGNORED_FILE_NAMES) {
         if (defFile.getName().matches(pattern) || genFile.getName().matches(pattern)) {
            // These files should be ignored in the diff. Don't treat differences found in
            //    these files as valid differences. 
            return true;
         }
      }

      String generatedName = "generatedProject";
      ArrayList<String> defPath = new ArrayList<String>();

      defPath.add(defFile.getName());
      File parent = defFile.getParentFile();
      while (parent != null) {
         defPath.add(0, parent.getName());
         parent = parent.getParentFile();
      }

      String projectName = "";
      for (int i = 0; i < defPath.size(); i++) {
         if (defPath.get(i).contains("com.ngc.")) {
            projectName = defPath.get(i);
            break;
         }
      }

      BufferedReader defaultBr = new BufferedReader(new FileReader(defFile));
      BufferedReader generateBr = new BufferedReader(new FileReader(genFile));

      try {
         String defaultFileLine;
         String generatedFileLine;
         int lineNum = 0;
         while ((defaultFileLine = defaultBr.readLine()) != null
                && (generatedFileLine = generateBr.readLine()) != null) {
            lineNum++;

            defaultFileLine = removeComments(defaultFileLine);
            generatedFileLine = removeComments(generatedFileLine);

            // It isn't really an error if the only difference is the project name not matching with "generateProject".
            // This is expected. So let's work around this by temporarily changing the generatedProject files
            // to say the default project name, rather than 'generatedProject'.
            boolean switchNames = false;
            if (defaultFileLine.contains(projectName) && generatedFileLine.contains(generatedName)) {
               generatedFileLine = generatedFileLine.replace(generatedName, projectName);
               switchNames = true;
            }

            if (!defaultFileLine.equals(generatedFileLine)) {

               if (switchNames) {
                  generatedFileLine = generatedFileLine.replace(projectName, generatedName);
               }
               equalFiles = false;
               prettyPrintFilesNotEqual(defFile, genFile, lineNum, defaultFileLine, generatedFileLine);
            }

            if (switchNames) {
               generatedFileLine = generatedFileLine.replace(projectName, generatedName);
            }
         }
      } finally {
         defaultBr.close();
         generateBr.close();
      }

      return equalFiles;
   }

   /**
    * Removes the comments from the lines. Differences in comments can be ignored.
    *
    * @param fileLine the string line to remove the comments from
    * @return the fileLine with comments replaced with ""
    */
   private static String removeComments(String fileLine) {
      if (fileLine.contains("#")) {
         String toRemove = fileLine.substring(fileLine.indexOf("#"), fileLine.length());
         fileLine = fileLine.replace(toRemove, "");
      }

      if (fileLine.contains("//")) {
         String toRemove = fileLine.substring(fileLine.indexOf("//"), fileLine.length());
         fileLine = fileLine.replace(toRemove, "");
      }

      if (fileLine.contains("/*") && fileLine.contains("*/")) {
         String toRemove = fileLine.substring(fileLine.indexOf("/*"), fileLine.indexOf("*/"));
         fileLine = fileLine.replace(toRemove, "");
      }

      return fileLine;
   }

   /**
    * Method to display a user friendly message indicating that the files do not contain equal contents
    *
    * @param defFile     - the evaluated file in the given project
    * @param genFile     - the evaluated file in the generated project
    * @param diffLineNum - the line number where the difference was observed
    * @param defLine     - the contents of the evaluated line in the given project
    * @param genLine     - the contents of the evaluated line in the generated project
    */
   private static void prettyPrintFilesNotEqual(File defFile, File genFile, int diffLineNum,
                                                String defLine, String genLine) {
      System.out.println("--------------------\nFiles are not equal. Valid differences found:");
      System.out.println("Default File: " + defFile.getAbsolutePath());
      System.out.println("Generated File: " + genFile.getAbsolutePath() + "\n");

      System.out.println("Line " + diffLineNum + ":");
      System.out.println("Default file\t: " + defLine);
      System.out.println("Generated file\t: " + genLine + "\n");
   }

   /**
    * Method to display a user friendly message indicating that the files under investigation are of different types
    *
    * @param file1 - a file in the given project
    * @param file2 - a file in the generated project
    */
   private static void prettyPrintFilesDifferentType(File file1, File file2) {
      if (file1.isFile() && file2.isDirectory()) {
         System.out.println("File " + file1.getName() + " is a FILE and File " + file2.getName() + " is a DIRECTORY");
      } else {
         System.out.println("File " + file1.getName() + " is a DIRECTORY and File " + file2.getName() + " is a FILE");
      }
   }

   /**
    * Convenience method to read the provided Jellyfish Properties file
    *
    * @param dir directory of the properties file
    * @return a map with property name keys and the corresponding property values
    * @throws IOException working with libraries to read a file on the filesystem
    */
   private static Map<String, String> readJellyfishPropertiesFile(String dir) throws IOException {
      Map<String, String> props = new HashMap<String, String>();
      File fin = new File(dir, "jellyfish.properties");
      FileInputStream fis = new FileInputStream(fin);

      // Construct BufferedReader from InputStreamReader
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));

      String line = null;
      while ((line = br.readLine()) != null) {
         String[] splitLine = line.split("=");
         for (int i = 0; i < splitLine.length; i++) {
            props.put(splitLine[0], splitLine[1]);
         }
      }

      br.close();
      return props;
   }

   /**
    * Convenience method to delete a directory
    *
    * @param file the directory to delete
    */
   static void deleteDir(File file) {
      File[] contents = file.listFiles();
      if (contents != null) {
         for (File f : contents) {
            deleteDir(f);
         }
      }
      file.delete();
   }

}
