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
