package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedSystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * The parsing delegate does the actual work of parsing system descriptor files.
 */
public class ParsingDelegate {

   private final ILogService logService;
   private final ParsingUtils utils;

   /**
    * Creates a new parsing delegate.
    */
   @Inject
   public ParsingDelegate(ILogService logService, IRepositoryService repositoryService) {
      this.logService = Preconditions.checkNotNull(logService, "logService may not be null!");
      this.utils = new ParsingUtils(repositoryService);
   }

   /**
    * Parses a system descriptor project located at the given directory.
    *
    * @param projectDirectory the directory that contains the system descriptor project
    * @return the results of parsing the project
    */
   public IParsingResult parseProject(Path projectDirectory) {
      Preconditions.checkNotNull(projectDirectory, "projectDirectory may not be null!");
      Preconditions.checkArgument(Files.isDirectory(projectDirectory), "%s is not a directory!", projectDirectory);

      try (ParsingContext ctx = new ParsingContext()) {
         Collection<XtextResource> resources = utils.getProjectAndDependencies(projectDirectory, ctx);
         return getResult(ctx, resources);
      } catch (Exception e) {
         throw new ParsingException(e.getMessage(), e);
      }
   }

   /**
    * Parses a system descriptor project identified by the given group ID, artifact ID, and version.
    *
    * @param gav the group ID, artifact ID, and version in the format {@code groupId:artifactId:version}
    * @return the results of parsing the project
    */
   public IParsingResult parseProject(String gav) {
      Preconditions.checkNotNull(gav, "gav may not be null!");
      Preconditions.checkArgument(gav.matches("[^:\\s]+:[^:\\s]+:[^:\\s]+"), "invalid gav: " + gav);
      try (ParsingContext ctx = new ParsingContext()) {
         Collection<XtextResource> resources = utils.getProjectAndDependencies(gav, ctx);
         return getResult(ctx, resources);
      } catch (Exception e) {
         throw new ParsingException(e.getMessage(), e);
      }
   }

   /**
    * Parses the given system descriptor files.
    *
    * @param paths the files to parse
    * @return the results of parsing the files
    */
   public IParsingResult parseFiles(Collection<Path> paths) {
      Preconditions.checkNotNull(paths, "paths may not be null!");
      Preconditions.checkArgument(!paths.isEmpty(), "paths is empty!");

      XTextParsingResult result;

      Stopwatch timer = Stopwatch.createStarted();
      try (ParsingContext ctx = new ParsingContext()) {
         // Create all the resources.
         Collection<XtextResource> resources = getResources(paths, ctx);
         // Now aggregate the validation results.
         result = getResult(ctx, resources);
      }
      timer.stop();

      if (result.isSuccessful()) {
         logService.debug(getClass(),
                          "Successfully parsed %s files in %d ms.",
                          paths.size(),
                          timer.elapsed(TimeUnit.MILLISECONDS));
      } else {
         logService.debug(getClass(),
                          "Parsed %s files in %d ms but %d issues detected.",
                          paths.size(),
                          timer.elapsed(TimeUnit.MILLISECONDS),
                          result.getIssues().size());
      }

      return result;
   }

   private Collection<XtextResource> getResources(Collection<Path> paths, ParsingContext ctx) {
      Collection<XtextResource> resources = new ArrayList<>();
      for (Path path : paths) {
         if (path.getFileName().toString().toLowerCase().endsWith(".zip")) {
            try {
               resources.addAll(ParsingUtils.parseJar(path, ctx));
            } catch (IOException e) {
               throw new ParsingException(e);
            }
         } else {
            resources.add(ctx.resourceOf(path));
         }
      }
      return resources;
   }

   private XTextParsingResult getResult(ParsingContext context, Collection<XtextResource> resources) {
      XTextParsingResult result = new XTextParsingResult();

      // This loop is important.  Up until this point, we have not actually loaded any resources.  We have waited until
      // all the resources are registered with the resource set.  This ensures that cross references will be resolved
      // correctly.  Calling getResource(uri, true) will force the loading of that resource.
      for (XtextResource resource : resources) {
         // Force resolution of all proxy objects.
         resource.getResourceSet().getResource(resource.getURI(), true);
      }

      // Now get the results of validation.
      Iterator<XtextResource> i = resources.iterator();
      XtextResource resource = i.next();
      // Get the validator.
      IResourceValidator validator = resource.getResourceServiceProvider().getResourceValidator();
      // If the parsing was success, create a wrapped system descriptor from the first object. The wrapper will
      // find all the packages that were parsed.
      if (!resource.getContents().isEmpty()) {
         // A single file contains at most one package.
         Package p = (Package) resource.getContents().get(0);
         result.setSystemDescriptor(new WrappedSystemDescriptor(p));
      }

      // Aggregate the remaining issues.
      do {
         result.addIssues(validator.validate(resource, CheckMode.ALL, null));
         resource = i.hasNext() ? i.next() : null;
      } while (resource != null);

      return result;
   }
}
