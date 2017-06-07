package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.io.Closeables;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedSystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.IResourceScopeCache;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The parsing delegate does the actual work of parsing system descriptor files.
 */
public class ParsingDelegate {

   /**
    * The default path that contains the {@code .sd} files for a standard system descriptor project.
    */
   private static final Path SD_SOURCE_PATH = Paths.get("src", "main", "sd");

   /**
    * The XText parser that can parse files.
    */
   private final IParser parser;

   /**
    * Used for logging.
    */
   private final ILogService logService;

   @Inject
   public ParsingDelegate(IParser parser, ILogService logService) {
      this.parser = Preconditions.checkNotNull(parser, "parser may not be null!");
      this.logService = Preconditions.checkNotNull(logService, "logService may not be null!");
   }

   public IParsingResult parseProject(Path projectDirectory) {
      Preconditions.checkNotNull(projectDirectory, "projectDirectory may not be null!");
      Preconditions.checkArgument(Files.isDirectory(projectDirectory), "%s is not a directory!", projectDirectory);
      Path src = projectDirectory.resolve(SD_SOURCE_PATH);
      Preconditions.checkArgument(Files.isDirectory(src),
                                  "%s is not a directory, project does not contain correct file structure!",
                                  src);

      IParsingResult result;
      try {
         PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.sd");
         Collection<Path> sdFiles = Files.find(
               src,
               Integer.MAX_VALUE,
               (path, basicFileAttributes) -> matcher.matches(path.getFileName()))
               .collect(Collectors.toList());
         result = parseFiles(sdFiles);
      } catch (IOException e) {
         throw new ParsingException(e.getMessage(), e);
      }

      return result;
   }

   public IParsingResult parseFiles(Collection<Path> paths) {
      Preconditions.checkNotNull(paths, "paths may not be null!");
      Preconditions.checkArgument(!paths.isEmpty(), "paths is empty!");

      XTextParsingResult result;
      ParsingContext ctx = new ParsingContext();

      Stopwatch timer = Stopwatch.createStarted();
      try {
         // Create all the resources.
         Collection<XtextResource> resources = getResources(paths, ctx);
         // Now aggregate the validation results.
         result = getResult(ctx, resources);
      } catch (IOException e) {
         logService.error(getClass(), "Error while parsing content in %s!", paths);
         throw new ParsingException(e.getMessage(), e);
      } finally {
         ctx.close();
      }
      timer.stop();

      if (result.isSuccessful()) {
         logService.debug(getClass(), "Successfully parsed %s files in %d ms.",
                          paths.size(),
                          timer.elapsed(TimeUnit.MILLISECONDS));
      } else {
         logService.debug(getClass(), "Parsed %s files in %d ms but %d issues detected.",
                          paths.size(),
                          timer.elapsed(TimeUnit.MILLISECONDS),
                          result.getIssues().size());
      }

      return result;
   }

   private Collection<XtextResource> getResources(Collection<Path> paths, ParsingContext ctx) throws IOException {
      Collection<XtextResource> resources = new ArrayList<>();
      for (Path path : paths) {
         resources.add(ctx.resourceOf(path));
      }
      return resources;
   }

   private XTextParsingResult getResult(ParsingContext ctx, Collection<XtextResource> resources) {
      XTextParsingResult result = new XTextParsingResult();

      // We can't do the validation until all the resources are added to the
      // set (in order to ensure imports are resolved).
      Iterator<XtextResource> i = resources.iterator();
      XtextResource resource = i.next();
      // Get the validator.
      IResourceValidator validator = resource.getResourceServiceProvider().getResourceValidator();
      // If the parsing was success, create a wrapped system descriptor from the first object.  The wrapper will
      // find all the packages that were parsed.
      if (!resource.getContents().isEmpty()) {
         // A single file contains at most one package.
         Package p = (Package) resource.getContents().get(0);
         result.setSystemDescriptor(new WrappedSystemDescriptor(p));
      }

      // Aggregate the remaining issues.
      do {
         result.addIssues(validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl));
         resource = i.hasNext() ? i.next() : null;
      } while (i.hasNext());

      return result;
   }

   /**
    * Mains state and context for a single parsing invocation.
    */
   private class ParsingContext {

      private final Collection<InputStream> streams = new ArrayList<>();

      /**
       * The XText resource set we use to create resources that will be parsed.
       */
      private final XtextResourceSet resourceSet = new XtextResourceSet();

      private ParsingContext() {
         // Configure XText to resolve imports.
         this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
      }

      private InputStream streamOf(Path file) throws IOException {
         InputStream is = Files.newInputStream(file);
         streams.add(is);
         return is;
      }

      private XtextResource resourceOf(Path file) throws IOException {
         XtextResource r = (XtextResource) resourceSet.createResource(
               URI.createFileURI(file.toAbsolutePath().toFile().toString()));
         r.load(streamOf(file), resourceSet.getLoadOptions());
         return r;
      }

      private void close() {
         // Close all streams.
         streams.forEach(Closeables::closeQuietly);
         // Remove all resources.  If we don't do this, Xtext will hold on to them.
         for (Resource r : resourceSet.getResources()) {
            r.unload();
         }
         resourceSet.getResources().clear();
      }
   }
}
