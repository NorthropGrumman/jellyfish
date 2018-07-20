package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;

import java.nio.file.Path;
import java.util.Set;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class RepositoryServiceGuiceWrapper implements IRepositoryService {

   private final RepositoryService delegate = new RepositoryService();

   @Inject
   public RepositoryServiceGuiceWrapper(ILogService logService, GradlePropertiesService propertiesService) {
      delegate.setLogService(logService);
      delegate.setPropertiesService(propertiesService);
      delegate.activate();
   }

   @Override
   public Path getArtifact(String identifier) {
      return delegate.getArtifact(identifier);
   }

   @Override
   public Set<Path> getArtifactDependencies(String identifier, boolean transitive) {
      return delegate.getArtifactDependencies(identifier, transitive);
   }
}
