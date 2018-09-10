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
package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.eclipse.aether.repository.RemoteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RemoteRepositoryServiceTest {

   private RepositoryService service;

   private GradlePropertiesService propertiesService = new GradlePropertiesService();

   @Before
   public void setup() {
      ILogService logService = new PrintStreamLogService();
      propertiesService.setLogService(logService);

      service = new RepositoryService();
      service.setLogService(logService);
      service.setPropertiesService(propertiesService);
   }

   @After
   public void cleanup() {
      service.deactivate();
      propertiesService.deactivate();
   }

   @Test
   public void testRemoteRepositoryValue() {
      final String NEXUS1 = "http://nexus1";
      final String NEXUS2 = "http://nexus2";
      Optional<RemoteRepository> repo;
      System.setProperty(GradlePropertiesService.GRADLE_USER_HOME, "src/test/resources");
      propertiesService.activate();
      service.activate();
      repo = service.findRemoteNexus();
      assertTrue(repo.isPresent());
      assertEquals(NEXUS1, repo.get().getUrl());
      System.setProperty(RepositoryService.NEXUS_CONSOLIDATED, NEXUS2);
      repo = service.findRemoteNexus();
      assertTrue(repo.isPresent());
      assertEquals(NEXUS2, repo.get().getUrl());
   }
}
