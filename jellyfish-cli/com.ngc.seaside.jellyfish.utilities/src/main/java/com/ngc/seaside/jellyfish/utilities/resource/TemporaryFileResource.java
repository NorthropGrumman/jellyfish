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
