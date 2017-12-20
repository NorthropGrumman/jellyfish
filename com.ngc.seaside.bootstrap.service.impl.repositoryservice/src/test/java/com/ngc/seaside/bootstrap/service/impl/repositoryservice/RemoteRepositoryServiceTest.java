package com.ngc.seaside.bootstrap.service.impl.repositoryservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.eclipse.aether.repository.RemoteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class RemoteRepositoryServiceTest {

   private RepositoryService service;

   @Before
   public void setup() {
      service = new RepositoryService();
      service.setLogService(new PrintStreamLogService());
      service.activate();
   }

   @After
   public void cleanup() {
      service.deactivate();
   }

   @Test
   public void testRemoteRepositoryValue() {
      final String NEXUS1 = "http://nexus1";
      final String NEXUS2 = "http://nexus2";
      Optional<RemoteRepository> repo;
      System.setProperty(RepositoryService.GRADLE_USER_HOME, "src/test/resources");
      repo = service.findRemoteNexus();
      assertTrue(repo.isPresent());
      assertEquals(NEXUS1, repo.get().getUrl());
      System.setProperty(RepositoryService.NEXUS_CONSOLIDATED, NEXUS2);
      repo = service.findRemoteNexus();
      assertTrue(repo.isPresent());
      assertEquals(NEXUS2, repo.get().getUrl());
   }
}
