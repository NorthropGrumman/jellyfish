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
   public boolean readResource(IReadableResource iReadableResource) throws ResourceServiceException {
      return delegate.readResource(iReadableResource);
   }

   @Override
   public boolean readOptionalResource(IReadableResource iReadableResource) {
      return delegate.readOptionalResource(iReadableResource);
   }

   @Override
   public boolean readFileResource(IReadableFileResource iReadableFileResource) throws ResourceServiceException {
      return delegate.readFileResource(iReadableFileResource);
   }

   @Override
   public boolean readOptionalFileResource(IReadableFileResource iReadableFileResource) {
      return delegate.readOptionalFileResource(iReadableFileResource);
   }

   @Override
   public boolean writeFileResource(IWritableFileResource iWritableFileResource) throws ResourceServiceException {
      return delegate.writeFileResource(iWritableFileResource);
   }

   @Override
   public boolean exists(IFileResource iFileResource) {
      return delegate.exists(iFileResource);
   }

}
