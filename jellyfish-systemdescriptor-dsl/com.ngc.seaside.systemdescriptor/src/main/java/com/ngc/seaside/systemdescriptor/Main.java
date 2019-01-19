/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This is a simple command line to validate and set of system descriptor files.
 * It is mostly used for debugging.
 */
public class Main {

   private static final Path SD_SOURCE_PATH = Paths.get("src", "main", "sd");

   private final Injector injector;

   public static void main(String[] args) {
      new Main(args);
   }

   private Main(String[] args) {
      Preconditions.checkArgument(args.length > 0, "please supply a SD project directory or list of SD files!");

      Collection<File> files = new ArrayList<>();
      for (String arg : args) {
         Path path = Paths.get(arg);
         if (Files.isDirectory(path)) {
            files.addAll(findFiles(path));
         } else if (Files.exists(path)) {
            files.add(path.toAbsolutePath().toFile());
         } else {
            System.err.format("%s does not exist!%n", path);
         }
      }

      injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();

      printIssues(getIssues(files));
   }

   private static Collection<File> findFiles(Path projectDirectory) {
      Path src = projectDirectory.resolve(SD_SOURCE_PATH);
      Preconditions.checkArgument(Files.isDirectory(src), "%s is not a directory or does not exist!", src);

      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.sd");
      Collection<File> sdFiles;
      try {
         sdFiles = Files.find(
               src,
               Integer.MAX_VALUE,
               (path, basicFileAttributes) -> matcher.matches(path.getFileName()))
               .map(p -> p.toAbsolutePath().toFile())
               .collect(Collectors.toList());
      } catch (IOException e) {
         throw new RuntimeException(e.getMessage(), e);
      }

      System.out.format("Found %d SD files:%n", sdFiles.size());
      sdFiles.forEach(f -> System.out.println(f.getAbsolutePath()));
      return sdFiles;
   }

   private Collection<Issue> getIssues(Collection<File> files) {
      XtextResourceSet resourceSet = injector.getProvider(XtextResourceSet.class).get();
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

      Collection<XtextResource> resources = new ArrayList<>(files.size());
      for (File f : files) {
         resources.add((XtextResource) resourceSet.getResource(URI.createFileURI(f.getAbsolutePath()), true));
      }

      Collection<Issue> issues = new ArrayList<>();
      for (XtextResource resource : resources) {
         IResourceValidator validator = resource.getResourceServiceProvider().getResourceValidator();
         issues.addAll(validator.validate(resource, CheckMode.ALL, null));
      }

      return issues;
   }

   private static void printIssues(Collection<Issue> issues) {
      System.out.format("Found %d issues:%n", issues.size());
      issues.forEach(i -> System.out
            .format("%s: %s: line %d - %s%n", i.getSeverity(), i.getUriToProblem(), i.getLineNumber(), i.getMessage()));
   }
}
