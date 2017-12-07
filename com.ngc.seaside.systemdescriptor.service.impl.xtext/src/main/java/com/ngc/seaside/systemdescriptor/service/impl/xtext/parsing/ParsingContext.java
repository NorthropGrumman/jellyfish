package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.common.io.Closeables;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
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

   public ParsingContext() {
      // Configure XText to resolve imports.
      this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }

   private InputStream streamOf(Path file) throws IOException {
      InputStream is = Files.newInputStream(file);
      streams.add(is);
      return is;
   }
   
   public XtextResource resourceOf(URI uri, InputStream stream) throws IOException {
      XtextResource r = (XtextResource) resourceSet.createResource(uri);
      ByteArrayOutputStream out = new ByteArrayOutputStream(stream.available());
      IOUtils.copy(stream, out);
      stream = new ByteArrayInputStream(out.toByteArray());
      if (r != null) {
         r.load(stream, resourceSet.getLoadOptions());
         System.out.println(uri);
      }
      
      return r;
   }

   public XtextResource resourceOf(Path file) throws IOException {
      XtextResource r = (XtextResource) resourceSet.createResource(
            URI.createFileURI(file.toAbsolutePath().toFile().toString()));
      r.load(streamOf(file), resourceSet.getLoadOptions());
      System.out.println(file);
      return r;
   }

   @Override
   public void close() {
      streams.forEach(Closeables::closeQuietly);
   }
}