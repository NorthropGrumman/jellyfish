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

   private final Collection<InputStream> streams = new ArrayList<>();

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
      streams.forEach(Closeables::closeQuietly);
   }

   /**
    * Creates a new resource using the given URI and copies the contents of the stream to this resource.
    *
    * @param uri    the URI if the new resource to create
    * @param stream the stream that contains the contents of the resource
    * @return the new resource
    * @throws IOException if an errors occurs during copying
    */
   public XtextResource resourceOf(URI uri, InputStream stream) throws IOException {
      XtextResource r = (XtextResource) resourceSet.createResource(uri);
      ByteArrayOutputStream out = new ByteArrayOutputStream(stream.available());
      IOUtils.copy(stream, out);
      stream = new ByteArrayInputStream(out.toByteArray());
      if (r != null) {
         r.load(stream, resourceSet.getLoadOptions());
      }

      return r;
   }

   /**
    * Creates a new resource and loads the content of the given file into it.
    *
    * @param file the file to load
    * @return the new resource that contains the contents of {@code file}
    * @throws IOException if the contents of the file cannot be loaded
    */
   public XtextResource resourceOf(Path file) throws IOException {
      XtextResource r = (XtextResource) resourceSet.createResource(
            URI.createFileURI(file.toAbsolutePath().toFile().toString()));
      r.load(streamOf(file), resourceSet.getLoadOptions());
      return r;
   }

   private InputStream streamOf(Path file) throws IOException {
      InputStream is = Files.newInputStream(file);
      streams.add(is);
      return is;
   }
}
