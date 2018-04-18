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
   public boolean readResource(IReadableResource ireadableresource) throws ResourceServiceException {
      return delegate.readResource(ireadableresource);
   }

   @Override
   public boolean readOptionalResource(IReadableResource ireadableresource) {
      return delegate.readOptionalResource(ireadableresource);
   }

   @Override
   public boolean readFileResource(IReadableFileResource ireadableresource) throws ResourceServiceException {
      return delegate.readFileResource(ireadableresource);
   }

   @Override
   public boolean readOptionalFileResource(IReadableFileResource ireadableresource) {
      return delegate.readOptionalFileResource(ireadableresource);
   }

   @Override
   public boolean writeFileResource(IWritableFileResource iwritablefileresource) throws ResourceServiceException {
      return delegate.writeFileResource(iwritablefileresource);
   }

   @Override
   public boolean exists(IFileResource ifileresource) {
      return delegate.exists(ifileresource);
   }

}
