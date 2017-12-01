package com.ngc.seaside.jellyfish.examples.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RegressionDiffTask extends DefaultTask {

   private String regression;
   private File expectedDir;
   private File generatedDir;

   private Collection<String> excludedFileRegularExpressions = new ArrayList<>(Arrays.asList(
      ".*\\.bin$",
      ".*\\.jar$",
      ".*\\.lock$",
      ".*\\.class$",
      ".*\\.tar$",
      ".*\\.tar\\.gz$",
      ".*\\.tgz",
      ".*\\.html?$",
      ".*\\.zip$",
      ".*javadoc.*",
      "(.*[\\\\/])?tmp([\\\\/].*)?",
      "(.*[\\\\/])?\\.gradle([\\\\/].*)?"));

   public String getRegression() {
      return regression;
   }

   public RegressionDiffTask setRegression(String regression) {
      this.regression = regression;
      return this;
   }

   public File getExpectedDir() {
      return expectedDir;
   }

   public RegressionDiffTask setExpectedDir(File file) {
      this.expectedDir = file;
      return this;
   }

   public File getGeneratedDir() {
      return generatedDir;
   }

   public RegressionDiffTask setGeneratedDir(File file) {
      this.generatedDir = file;
      return this;
   }

   public Collection<String> getExcludes() {
      return excludedFileRegularExpressions;
   }

   public RegressionDiffTask setExcludes(Collection<String> excludes) {
      this.excludedFileRegularExpressions = excludes == null ? new ArrayList<>() : new ArrayList<>(excludes);
      return this;
   }

   public RegressionDiffTask exclude(Iterable<String> excludes) {
      excludes.forEach(this.excludedFileRegularExpressions::add);
      return this;
   }

   public RegressionDiffTask excludes(String... excludes) {
      this.excludedFileRegularExpressions.addAll(Arrays.asList(excludes));
      return this;
   }

   @TaskAction
   public void executeTask() throws IOException {
      if (!compareDirectories(expectedDir, generatedDir)) {
         Assert.fail("Regression " + getRegression() + " did not generate the expected output");
      }
   }

   /**
    * Recursively search through the project tree, comparing directories and files along the way. Return a false value
    * if a mismatch is found.
    *
    * @param expectedDir the folder corresponding to the expected project
    * @param generatedDir the folder corresponding to the generated project
    * @return a boolean value indicating that the directory tree is equal
    * @throws IOException Working with FileUtils library
    */
   private boolean compareDirectories(File expectedDir, File generatedDir) throws IOException {

      boolean sizeEquality = true;
      boolean typeEquality = true;
      boolean fileEquality = true;
      boolean dirEquality = true;

      ArrayList<File> defaultFiles = new ArrayList<File>();
      ArrayList<File> genProjFiles = new ArrayList<File>();
      for (File file : expectedDir.listFiles()) {
         defaultFiles.add(file);
      }

      for (File file : generatedDir.listFiles()) {
         genProjFiles.add(file);
      }
      defaultFiles.sort(null);
      genProjFiles.sort(null);

      // The trees must contain the same number of elements to be equal
      if (defaultFiles.size() != genProjFiles.size()) {
         prettyPrintFolderContainDifferentListFiles(expectedDir, generatedDir);
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
      System.err.println("--------------------------\nDirectories are not equal:");

      File[] subDir1 = defProj.listFiles();
      File[] subDir2 = genProj.listFiles();

      System.err.println("Folder: " + defProj.getAbsolutePath());
      for (int i = 0; i < subDir1.length; i++) {
         System.err.println("\t- " + subDir1[i].getName());
      }

      System.err.println("\nFolder: " + genProj.getAbsolutePath());
      for (int i = 0; i < subDir2.length; i++) {
         System.out.println("\t- " + subDir2[i].getName());
      }

      System.err.println();
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
   private boolean validFileDifferences(File defFile, File genFile) throws IOException {
      boolean equalFiles = true;

      for (String pattern : excludedFileRegularExpressions) {
         if (defFile.getName().matches(pattern) || genFile.getName().matches(pattern)) {
            // These files should be ignored in the diff. Don't treat differences found in
            // these files as valid differences.
            return true;
         }
      }

      ArrayList<String> defPath = new ArrayList<String>();

      defPath.add(defFile.getName());
      File parent = defFile.getParentFile();
      while (parent != null) {
         defPath.add(0, parent.getName());
         parent = parent.getParentFile();
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

            if (!defaultFileLine.equals(generatedFileLine)) {
               equalFiles = false;
               prettyPrintFilesNotEqual(defFile, genFile, lineNum, defaultFileLine, generatedFileLine);
            }

         }
      } finally {
         defaultBr.close();
         generateBr.close();
      }

      return equalFiles;
   }

   /**
    * Method to display a user friendly message indicating that the files do not contain equal contents
    *
    * @param defFile - the evaluated file in the given project
    * @param genFile - the evaluated file in the generated project
    * @param diffLineNum - the line number where the difference was observed
    * @param defLine - the contents of the evaluated line in the given project
    * @param genLine - the contents of the evaluated line in the generated project
    */
   private static void prettyPrintFilesNotEqual(File defFile, File genFile, int diffLineNum,
            String defLine, String genLine) {
      System.err.println("--------------------\nFiles are not equal. Valid differences found:");
      System.err.println("  Default File: " + defFile.getAbsolutePath());
      System.err.println("Generated File: " + genFile.getAbsolutePath() + "\n");

      System.err.println("Line " + diffLineNum + ":");
      System.err.println("  Default file: " + defLine);
      System.err.println("Generated file: " + genLine + "\n");
   }

   /**
    * Method to display a user friendly message indicating that the files under investigation are of different types
    *
    * @param file1 - a file in the given project
    * @param file2 - a file in the generated project
    */
   private static void prettyPrintFilesDifferentType(File file1, File file2) {
      if (file1.isFile() && file2.isDirectory()) {
         System.err.println("File " + file1.getName() + " is a FILE and File " + file2.getName() + " is a DIRECTORY");
      } else {
         System.err.println("File " + file1.getName() + " is a DIRECTORY and File " + file2.getName() + " is a FILE");
      }
   }

}
