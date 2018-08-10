package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

/**
 * Mains state and context for a single parsing invocation.
 */
public class ParsingContext implements AutoCloseable {

   /**
    * The XText resource set we use to create resources that will be parsed.
    */
   private final XtextResourceSet resourceSet = new XtextResourceSet();
   private Path main;
   private Path test;

   /**
    * Contains the resources already in the resource set keyed by URI.
    */
   private final Map<URI, XtextResource> resources = new HashMap<>();

   /**
    * Creates a new context.
    */
   public ParsingContext() {
      // Configure XText to resolve imports.
      this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
   }

   public Path getMain() {
      return main;
   }

   public void setMain(Path main) {
      this.main = main;
   }

   public Path getTest() {
      return test;
   }

   public void setTest(Path test) {
      this.test = test;
   }

   @Override
   public void close() {
      // Do nothing.
   }

   /**
    * Creates a new resource using the given ZIP entry and contents of the stream.
    *
    * @param zipFile the ZIP file
    * @param entry   the ZIP entry
    * @return the new resource
    */
   public XtextResource resourceOf(Path zipFile, ZipEntry entry) {
      // Do not load the resource here.  If we do that, validation will automatically start.  This can cause problems
      // if all the resources have not yet been added to the set.
      URI uri = URI.createHierarchicalURI(
            // scheme (this must be 'archive' or 'jar' for ZIPs)
            "archive",
            // authority (this is the path to the JAR/ZIP followed by '!')
            URI.createFileURI(zipFile.toAbsolutePath().toFile().toString()).toString() + "!",
            // device (must be null for ZIPs/JARs)
            null,
            // segments (the array of strings that make up the path to the file in the ZIP/JAR)
            entry.getName().split("/"),
            // query (ZIPs/JARs don't have query parameters)
            null,
            // fragment (ZIPs/JARs can't have fragments)
            null
      );
      // Only create the resource if the resource is not already in the set.  This avoid errors that have to do with
      // dependency management of System Descriptor projects.  In particular, we could try to add the same resource
      // multiple times if two or more projects reference the same dependency.
      return resources.computeIfAbsent(uri, key -> (XtextResource) resourceSet.createResource(key));
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
      URI uri = URI.createFileURI(file.toAbsolutePath().toFile().toString());
      // Only create the resource if the resource is not already in the set.  This avoid errors that have to do with
      // dependency management of System Descriptor projects.  In particular, we could try to add the same resource
      // multiple times if two or more projects reference the same dependency.
      return resources.computeIfAbsent(uri, key -> (XtextResource) resourceSet.createResource(key));
   }
}
