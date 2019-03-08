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
