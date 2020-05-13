/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
