package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedSystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class XTextSystemDescriptorService implements ISystemDescriptorService {

   private final Injector injector;

   @Inject
   private IParser parser;

   @Inject
   private XtextResourceSet resourceSet;

   public XTextSystemDescriptorService() {
      this.injector = newInjector();
      injector.injectMembers(this);
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }

   @Override
   public IParsingResult parseProject(Path projectDirectory) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IParsingResult parseFiles(Collection<Path> paths) {
      Preconditions.checkNotNull(paths, "paths may not be null!");
      Preconditions.checkArgument(!paths.isEmpty(), "paths is empty!");

      XTextParsingResult result = new XTextParsingResult();
      ParsingContext ctx = new ParsingContext();

      try {
         // Create all the resources.
         Collection<XtextResource> resources = new ArrayList<>();
         for (Path path : paths) {
            resources.add(ctx.resourceOf(path));
         }

         // Now aggregate the validation results.  We can't do the validation until all the resources are added to the
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
      } catch (IOException e) {
         throw new ParsingException(e.getMessage(), e);
      } finally {
         ctx.close();
      }

      return result;
   }

   @Override
   public ISystemDescriptor immutableCopy(ISystemDescriptor descriptor) {
      throw new UnsupportedOperationException("not implemented");
   }

   protected Injector newInjector() {
      return new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
   }

   private class ParsingContext {

      private final Collection<InputStream> streams = new ArrayList<>();

      private InputStream streamOf(Path file) throws IOException {
         InputStream is = Files.newInputStream(file);
         streams.add(is);
         return is;
      }

      private XtextResource resourceOf(Path file) throws IOException {
         XtextResource r = (XtextResource) resourceSet.createResource(
               URI.createURI("dummy:/" + file.toAbsolutePath().toString()));
         r.load(streamOf(file), resourceSet.getLoadOptions());
         return r;
      }

      private void close() {
         streams.forEach(Closeables::closeQuietly);
      }
   }
}
