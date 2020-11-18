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
package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.eclipse.aether.repository.RemoteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.log.api.PrintStreamLogService;

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
