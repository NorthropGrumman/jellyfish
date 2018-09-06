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
package com.ngc.seaside.jellyfish.examples.tasks;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class RegressionDiffTask extends DefaultTask {

   private String regression;
   private File expectedDir;
   private File generatedDir;

   private Collection<String> excludedFileRegularExpressions = new ArrayList<>(Arrays.asList(
         ".*\\.bin$",
         ".*\\.bnd$",
         ".*\\.jar$",
         ".*\\.lock$",
         ".*\\.class$",
         ".*\\.tar$",
         ".*\\.tar\\.gz$",
         ".*\\.tgz",
         ".*\\.html?$",
         ".*\\.zip$",
         ".*javadoc.*",
         "\\.cli-dependencies.md5",
         "build\\.generated\\.gradle",
         // Ignore anything inside generate-projects because that will always be the same anyway.
         ".*" + Pattern.quote(File.separatorChar + "generated-projects" + File.separatorChar) + ".*",
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
    * @param expectedDir  the folder corresponding to the expected project
    * @param generatedDir the folder corresponding to the generated project
    * @return a boolean value indicating that the directory tree is equal
    * @throws IOException Working with FileUtils library
    */
   private boolean compareDirectories(File expectedDir, File generatedDir) throws IOException {

      boolean sizeEquality = true;
      boolean typeEquality = true;
      boolean fileEquality = true;
      boolean dirEquality = true;

      ArrayList<File> expectedFiles = new ArrayList<File>();
      ArrayList<File> generatedFiles = new ArrayList<File>();
      outer:
      for (File file : expectedDir.listFiles()) {
         for (String pattern : excludedFileRegularExpressions) {
            if (file.getAbsolutePath().matches(pattern)) {
               continue outer;
            }
         }
         expectedFiles.add(file);
      }

      outer:
      for (File file : generatedDir.listFiles()) {
         for (String pattern : excludedFileRegularExpressions) {
            if (file.getAbsolutePath().matches(pattern)) {
               continue outer;
            }
         }
         generatedFiles.add(file);
      }
      expectedFiles.sort(null);
      generatedFiles.sort(null);

      // The trees must contain the same number of elements to be equal
      if (expectedFiles.size() != generatedFiles.size()) {
         prettyPrintFolderContainDifferentListFiles(expectedDir, generatedDir);
         sizeEquality = false;
      } else {
         // Loop through the elements and evaluate them in each project
         for (int i = 0; i < expectedFiles.size(); i++) {
            File subFileDefDir = expectedFiles.get(i);
            File subFileGenDir = generatedFiles.get(i);

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
   private void prettyPrintFolderContainDifferentListFiles(File defProj, File genProj) {
      getProject().getLogger().error("--------------------------\nDirectories are not equal:");

      File[] subDir1 = defProj.listFiles();
      File[] subDir2 = genProj.listFiles();

      getProject().getLogger().error("Folder: " + defProj.getAbsolutePath());
      for (int i = 0; i < subDir1.length; i++) {
         getProject().getLogger().error("\t- " + subDir1[i].getName());
      }

      getProject().getLogger().error("\nFolder: " + genProj.getAbsolutePath());
      for (int i = 0; i < subDir2.length; i++) {
         getProject().getLogger().error("\t- " + subDir2[i].getName());
      }
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

      ArrayList<String> defPath = new ArrayList<String>();

      defPath.add(defFile.getName());
      File parent = defFile.getParentFile();
      while (parent != null) {
         defPath.add(0, parent.getName());
         parent = parent.getParentFile();
      }

      BufferedReader expectedBr = new BufferedReader(new FileReader(defFile));
      BufferedReader generatedBr = new BufferedReader(new FileReader(genFile));

      try {
         String expectedFileLine;
         String generatedFileLine;
         int lineNum = 0;
         while ((expectedFileLine = expectedBr.readLine()) != null
                && (generatedFileLine = generatedBr.readLine()) != null) {
            lineNum++;

            if (!expectedFileLine.equals(generatedFileLine)) {
               equalFiles = false;
               prettyPrintFilesNotEqual(defFile, genFile, lineNum, expectedFileLine, generatedFileLine);
            }

         }
      } finally {
         expectedBr.close();
         generatedBr.close();
      }

      return equalFiles;
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
   private void prettyPrintFilesNotEqual(File defFile, File genFile, int diffLineNum,
                                         String defLine, String genLine) {
      getProject().getLogger().error("--------------------\nFiles are not equal. Valid differences found:");
      getProject().getLogger().error(" Expected File: " + defFile.getAbsolutePath());
      getProject().getLogger().error("Generated File: " + genFile.getAbsolutePath() + "\n");

      getProject().getLogger().error("Line " + diffLineNum + ":");
      getProject().getLogger().error(" Expected file: " + defLine);
      getProject().getLogger().error("Generated file: " + genLine + "\n");
   }

   /**
    * Method to display a user friendly message indicating that the files under investigation are of different types
    *
    * @param file1 - a file in the given project
    * @param file2 - a file in the generated project
    */
   private void prettyPrintFilesDifferentType(File file1, File file2) {
      if (file1.isFile() && file2.isDirectory()) {
         getProject().getLogger()
               .error("File " + file1.getName() + " is a FILE and File " + file2.getName() + " is a DIRECTORY");
      } else {
         getProject().getLogger()
               .error("File " + file1.getName() + " is a DIRECTORY and File " + file2.getName() + " is a FILE");
      }
   }

}
