/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

import com.ngc.blocs.service.resource.api.IReadableResource;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * A type of {@link IReadableResource} that can be used to copy a resource to a temporary file for later use.  This is
 * particularly useful when unpacking a resource from the classpath or JAR so it can be provided to a process that
 * expects an file as opposed to a stream.
 */
public interface ITemporaryFileResource extends IReadableResource {

   /**
    * Gets the path to the temporary file of this resource.  This method may only be used after a successful {@link
    * #read(InputStream)} invocation.
    */
   Path getTemporaryFile();
}
