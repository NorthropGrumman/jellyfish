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
package com.ngc.blocs.guice.module;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IFileResource;
import com.ngc.blocs.service.resource.api.IReadableFileResource;
import com.ngc.blocs.service.resource.api.IReadableResource;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.service.resource.api.IWritableFileResource;
import com.ngc.blocs.service.resource.api.ResourceServiceException;
import com.ngc.blocs.service.resource.impl.common.ResourceService;

import java.nio.file.Path;

/**
 *
 */
public class ResourceServiceDelegate implements IResourceService {

   private final ResourceService delegate = new ResourceService();

   @Inject
   public ResourceServiceDelegate(ILogService logService) {
      delegate.setLogService(logService);
      delegate.activate();
   }

   @Override
   public Path getResourceRootPath() {
      return delegate.getResourceRootPath();
   }

   @Override
   public boolean readResource(IReadableResource readableResource) throws ResourceServiceException {
      return delegate.readResource(readableResource);
   }

   @Override
   public boolean readOptionalResource(IReadableResource readableResource) {
      return delegate.readOptionalResource(readableResource);
   }

   @Override
   public boolean readFileResource(IReadableFileResource readableResource) throws ResourceServiceException {
      return delegate.readFileResource(readableResource);
   }

   @Override
   public boolean readOptionalFileResource(IReadableFileResource readableResource) {
      return delegate.readOptionalFileResource(readableResource);
   }

   @Override
   public boolean writeFileResource(IWritableFileResource writableFileResource) throws ResourceServiceException {
      return delegate.writeFileResource(writableFileResource);
   }

   @Override
   public boolean exists(IFileResource fileResource) {
      return delegate.exists(fileResource);
   }

}
