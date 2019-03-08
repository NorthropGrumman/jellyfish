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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinFeature;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ParserException;
import gherkin.ast.GherkinDocument;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 * Parses Gherkin feature files.  Only parses files that end with the extension {@code .feature}.
 */
public class CucumberGherkinService implements IGherkinService {

   private final Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());

   private final ILogService logService;

   @Inject
   public CucumberGherkinService(ILogService logService) {
      this.logService = logService;
   }

   @Override
   public IGherkinParsingResult parseRecursively(Path directoryContainingFeatureFiles,
                                                 ISystemDescriptor systemDescriptor) {
      Preconditions.checkNotNull(directoryContainingFeatureFiles, "directoryContainingFeatureFiles may not be null!");
      Preconditions.checkNotNull(systemDescriptor, "systemDescriptor may not be null!");
      Preconditions.checkArgument(Files.isDirectory(directoryContainingFeatureFiles),
                                  "%s should be a directory!",
                                  directoryContainingFeatureFiles.toAbsolutePath());

      logService.trace(getClass(), "Attempting to parse feature files in %s.", directoryContainingFeatureFiles);

      GherkinParsingResult result = new GherkinParsingResult();
      PathMatcher matcher = directoryContainingFeatureFiles.getFileSystem().getPathMatcher("glob:**.feature");
      try {
         Files.walk(directoryContainingFeatureFiles)
               .filter(Files::isRegularFile)
               .filter(matcher::matches)
               .forEach(f -> parseFeatureFile(f, directoryContainingFeatureFiles, result, systemDescriptor));
      } catch (IOException e) {
         throw new UncheckedIOException("error parsing feature files from directory "
                                        + directoryContainingFeatureFiles,
                                        e);
      }
      return result;
   }

   private void parseFeatureFile(Path featureFile,
                                 Path rootDirectory,
                                 GherkinParsingResult result,
                                 ISystemDescriptor systemDescriptor) {
      try (Reader reader = Files.newBufferedReader(featureFile)) {
         GherkinDocument doc = parser.parse(reader);
         result.addFeature(convert(doc, featureFile, rootDirectory, systemDescriptor));
      } catch (ParserException.CompositeParserException e) {
         if (e.errors.isEmpty()) {
            result.addIssue(GherkinParsingIssue.forParsingException(featureFile, e));
         } else {
            result.addIssue(GherkinParsingIssue.forParsingException(featureFile, e.errors.get(0)));
         }
      } catch (ParserException e) {
         result.addIssue(GherkinParsingIssue.forParsingException(featureFile, e));
      } catch (IOException e) {
         result.addIssue(GherkinParsingIssue.forException(featureFile, e));
      }
   }

   private IFeature convert(GherkinDocument doc,
                            Path featureFile,
                            Path rootDirectory,
                            ISystemDescriptor systemDescriptor) {
      Path relativePath = rootDirectory.relativize(featureFile);
      String packageName = relativePath.getParent().toString().replace(File.separatorChar, '.');
      String name = com.google.common.io.Files.getNameWithoutExtension(relativePath.getFileName().toString());
      IScenario scenario = findSdScenario(packageName, name, systemDescriptor);

      // If the feature file has no content, the Gherkin feature object is null.  To avoid issues, we'll use a special
      // empty (non-null) feature wrapper for client to consume easier.
      if (doc.getFeature() == null) {
         return GherkinFeature.emptyFeature(packageName, name, scenario, featureFile);
      } else {
         return new GherkinFeature(doc.getFeature(), featureFile)
               .setPackage(packageName)
               .setName(name)
               .setModelScenario(scenario);
      }
   }

   private static IScenario findSdScenario(String packageName, String name, ISystemDescriptor systemDescriptor) {
      String[] names = name.split("\\.");
      return names.length == 2
             ? systemDescriptor.findModel(packageName, names[0])
                   .flatMap(s -> s.getScenarios().getByName(names[1]))
                   .orElse(null)
             : null;
   }
}
