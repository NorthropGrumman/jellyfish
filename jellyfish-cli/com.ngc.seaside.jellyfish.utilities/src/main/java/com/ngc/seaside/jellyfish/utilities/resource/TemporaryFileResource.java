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
package com.ngc.seaside.jellyfish.utilities.resource;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TemporaryFileResource implements ITemporaryFileResource {

   private final URL url;
   private final String fileName;
   private Path temporaryFile;

   /**
    * Creates a new {@code TemporaryFileResource}.
    *
    * @param url               the URL to the resource
    * @param temporaryFileName the name of the temporary file (not this is not usually a path but just a name)
    */
   public TemporaryFileResource(URL url, String temporaryFileName) {
      Preconditions.checkNotNull(url, "url may not be null!");
      Preconditions.checkNotNull(temporaryFileName, "temporaryFileName may not be null!");
      Preconditions.checkArgument(!temporaryFileName.trim().isEmpty(), "temporaryFileName may not be empty!");
      this.url = url;
      this.fileName = temporaryFileName;
   }

   @Override
   public Path getTemporaryFile() {
      Preconditions.checkState(temporaryFile != null,
                               "temporaryFile not yet created, read(..) not invoked!");
      return temporaryFile;
   }

   @Override
   public URL getURL() {
      return url;
   }

   @Override
   public boolean read(InputStream stream) {
      boolean success = true;
      try {
         Path tempDirectory = Files.createTempDirectory("blocs");
         tempDirectory.toFile().deleteOnExit();
         temporaryFile = tempDirectory.resolve(fileName);
         Files.copy(stream, temporaryFile);
      } catch (IOException e) {
         success = false;
      }
      return success;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof TemporaryFileResource)) {
         return false;
      }
      TemporaryFileResource that = (TemporaryFileResource) o;
      return Objects.equals(url, that.url)
            && Objects.equals(temporaryFile, that.temporaryFile);
   }

   @Override
   public int hashCode() {
      return Objects.hash(url, temporaryFile);
   }

   /**
    * Creates a new {@code ITemporaryFileResource} from a resource loaded from the classpath.
    *
    * @param clazz        the class whose classloader will be used to open the resource
    * @param resourceName the name of the resource
    * @return a new {@code ITemporaryFileResource} for the given classpath resource
    */
   public static ITemporaryFileResource forClasspathResource(Class<?> clazz,
                                                             String resourceName) {
      Preconditions.checkNotNull(clazz, "clazz may not be null!");
      Preconditions.checkNotNull(resourceName, "resourceName may not be null!");
      Preconditions.checkArgument(!resourceName.trim().isEmpty(), "resourceName may not be empty!");
      URL url = clazz.getClassLoader().getResource(resourceName);
      Preconditions.checkArgument(url != null, "%s could not be loaded by class loader %s!",
                                  resourceName,
                                  clazz.getClassLoader());
      return new TemporaryFileResource(url, resourceName);
   }
}
