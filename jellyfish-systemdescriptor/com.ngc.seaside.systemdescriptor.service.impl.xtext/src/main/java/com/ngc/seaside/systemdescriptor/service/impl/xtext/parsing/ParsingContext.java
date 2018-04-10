package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.common.io.Closeables;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Mains state and context for a single parsing invocation.
 */
public class ParsingContext implements AutoCloseable {

   /**
    * The XText resource set we use to create resources that will be parsed.
    */
   private final XtextResourceSet resourceSet = new XtextResourceSet();

   /**
    * Creates a new context.
    */
   public ParsingContext() {
      // Configure XText to resolve imports.
      this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }

   @Override
   public void close() {
      // Do nothing.
   }

   /**
    * Creates a new resource using the given URI.
    *
    * @param uri    the URI if the new resource to create
    * @return the new resource
    */
   public XtextResource resourceOf(URI uri) {
      // Do not load the resource here.  If we do that, validation will automatically start.  This can cause problems
      // if all the resources have not yet been added to the set.
      return (XtextResource) resourceSet.createResource(uri);
   }

   /**
    * Creates a new resource and loads the content of the given file into it.
    *
    * @param file the file to load
    * @return the new resource that contains the contents of {@code file}
    */
   public XtextResource resourceOf(Path file) {
      // Do not load the resource here.  If we do that, validation will automatically start.  This can cause problems
      // if all the resources have not yet been added to the set.
      return (XtextResource) resourceSet.createResource(
            URI.createFileURI(file.toAbsolutePath().toFile().toString()));
   }
}
