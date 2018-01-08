package com.ngc.seaside.bootstrap.utilities.resource;

import com.ngc.blocs.service.resource.api.IReadableResource;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * A type of {@link IReadableResource} that can be used to copy a resource to a temporary file for later use.  This is
 * particularly useful when unpacking a resource from the classpath or JAR so it can be provided to a process that
 * expects and file as opposed to a stream.
 */
public interface ITemporaryFileResource extends IReadableResource {

   /**
    * Gets the path to the temporary file of this resource.  This method may only be used after a successful {@link
    * #read(InputStream)} invocation.
    */
   Path getTemporaryFile();
}
