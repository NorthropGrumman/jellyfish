package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.common.base.Preconditions;
import com.ngc.seaside.bootstrap.service.repository.api.IRepositoryService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.XtextResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class ParsingUtils {

   /**
    * The default path that contains the {@code .sd} files for a standard
    * system descriptor project.
    */
   private static final Path SD_SOURCE_PATH = Paths.get("src", "main", "sd");

   private static final Path SD_CLASSPATH = Paths.get("build", "resources", "main");

   private static final Path POM_PATH = Paths.get("build", "poms");

   private final IRepositoryService repositoryService;

   public ParsingUtils(IRepositoryService repositoryService) {
      this.repositoryService = repositoryService;
   }
   
   /**
    * Returns the parsed XtextResources for the given project.
    * 
    * @param projectDirectory directory of project
    * @param ctx parsing context
    * @return collection of parsed XtextResources
    */
   public Collection<XtextResource> getProjectAndDependencies(Path projectDirectory, ParsingContext ctx) {
      Preconditions.checkNotNull(projectDirectory, "project directory may not be null!");
      Preconditions.checkNotNull(ctx, "parsing context may not be null!");
      try {
         return parseGradleProject(projectDirectory, ctx);
      } catch (IOException e) {
         throw new ParsingException(e);
      }
   }

   /**
    * Returns the parsed XtextResources for the project with the given gav.
    * 
    * @param gav groupId:artifactId:version
    * @param ctx parsing context
    * @return collection of parsed XtextResources
    */
   public Collection<XtextResource> getProjectAndDependencies(String gav, ParsingContext ctx) {
      Preconditions.checkNotNull(gav, "gav may not be null!");
      Preconditions.checkArgument(gav.matches("[^:\\s]+:[^:\\s]+:[^:\\s]+"), "invalid gav: " + gav);
      Preconditions.checkNotNull(ctx, "parsing context may not be null!");
      Collection<XtextResource> resources = new LinkedHashSet<>();
      try {
         return parseDependencies(gav, ctx, true);
      } catch (IOException e) {
         throw new ParsingException(e);
      }
   }

   /**
    * Parses the gradle project in the given directory. This project will attempt to locate system descriptor files in the projectDirectory/build/resources/main; otherwise it will locate system
    * descriptor files in src/main/sd. Dependencies to the project are determined by the pom file found in projectDirectory/build/poms.
    * 
    * @param projectDirectory directory of project
    * @param ctx parsing context
    * @return collection of parsed XtextResources
    * @throws IOException
    */
   private Collection<XtextResource> parseGradleProject(Path projectDirectory, ParsingContext ctx)
      throws IOException {
      Path resourcesDirectory = projectDirectory.resolve(SD_CLASSPATH);
      if (!Files.isDirectory(resourcesDirectory)) {
         resourcesDirectory = projectDirectory.resolve(SD_SOURCE_PATH);
         if (!Files.isDirectory(resourcesDirectory)) {
            resourcesDirectory = projectDirectory;
         }
      }

      Path pom = projectDirectory.resolve(POM_PATH);
      if (Files.isDirectory(pom)) {
         List<Path> poms = Files.list(pom)
                                .filter(file -> file.toString().endsWith(".pom") || file.toString().endsWith(".xml"))
                                .collect(Collectors.toList());
         if (poms.isEmpty()) {
            pom = null;
         } else if (poms.size() == 1) {
            pom = poms.get(0);
         } else {
            pom = pom.resolve("pom-default.xml");
            if (!Files.isRegularFile(pom)) {
               pom = null;
            }
         }
      } else {
         pom = null;
      }

      Collection<XtextResource> resources = new LinkedHashSet<>();

      try {
         Files.walk(resourcesDirectory)
              .filter(Files::isRegularFile)
              .filter(file -> file.toString().endsWith(".sd"))
              .map(file -> {
                 try {
                    return ctx.resourceOf(file);
                 } catch (IOException e) {
                    throw new UncheckedIOException(e);
                 }
              })
              .forEach(resources::add);
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }

      if (pom != null) {
         resources.addAll(parseDependencies(pom, ctx, false));
      }
      return resources;
   }

   /**
    * Returns the parsed XtextResources contained in the given jar.
    * 
    * @param jar jar file
    * @param ctx parsing context
    * @return collection of parsed XtextResources
    * @throws IOException
    */
   private Collection<XtextResource> parseJar(Path jar, ParsingContext ctx) throws IOException {
      Collection<XtextResource> resources = new LinkedHashSet<>();
      try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(jar))) {
         ZipEntry entry;
         while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().endsWith(".sd")) {
               URI uri = URI.createURI(entry.getName());
               XtextResource resource = ctx.resourceOf(uri, zis);
               if (resource != null) {
                  resources.add(resource);
               }
            }
         }
      }
      return resources;
   }

   /**
    * Uses the given maven pom file to locate the corresponding project and its dependencies and returns the parsed XtextResources from them.
    * 
    * @param pom maven pom file
    * @param ctx parsing context
    * @param includeSelf if {@code true} include the main project represented in the pom; otherwise, include only the pom's dependencies
    * @return collection of parsed XtextResources
    * @throws IOException
    */
   private Collection<XtextResource> parseDependencies(Path pom, ParsingContext ctx, boolean includeSelf)
      throws IOException {

      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model model;

      try {
         model = reader.read(Files.newBufferedReader(pom));
      } catch (Exception e) {
         return Collections.emptySet();
      }

      String gav = String.format("%s:%s:%s", model.getGroupId(), model.getArtifactId(), model.getVersion());

      return parseDependencies(gav, ctx, includeSelf);
   }

   /**
    * Locates the project corresponding to the given gav and its dependencies and returns the parsed XtextResources from them.
    * 
    * @param gav project gav
    * @param ctx parsing context
    * @param includeSelf if {@code true} include the main project represented by the gav; otherwise, include only the pom's dependencies
    * @return collection of parsed XtextResources
    * @throws IOException
    */
   private Collection<XtextResource> parseDependencies(String gav, ParsingContext ctx, boolean includeSelf)
      throws IOException {
      Collection<XtextResource> resources = new LinkedHashSet<>();
      String[] splitGav = gav.split(":");
      String artifactGav = String.format("%s:%s:zip:%s", splitGav[0], splitGav[1], splitGav[2]);
      if (includeSelf) {
         resources.addAll(parseJar(repositoryService.getArtifact(artifactGav), ctx));
      }
      for (Path path : repositoryService.getArtifactDependencies(artifactGav, true)) {
         resources.addAll(parseJar(path, ctx));
      }
      return resources;
   }

}
