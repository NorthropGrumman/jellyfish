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
