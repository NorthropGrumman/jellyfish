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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryServiceTest {

   private Path repository = Paths.get("src", "test", "resources", "repository");

   private RepositoryService service;

   @Before
   public void setup() {
      ILogService logService = new PrintStreamLogService();
      GradlePropertiesService propertiesService = new GradlePropertiesService();
      propertiesService.setLogService(logService);
      propertiesService.activate();

      service = spy(RepositoryService.class);
      service.setLogService(new PrintStreamLogService());
      service.setPropertiesService(propertiesService);
      doReturn(Optional.of(repository)).when(service).findMavenLocal();
      doReturn(Optional.empty()).when(service).findRemoteNexus();
      service.activate();
   }

   @After
   public void cleanup() {
      service.deactivate();
   }

   @Test
   public void testRepositoryService() {
      Path artifact = service.getArtifact("com.ngc.seaside:test.project1:zip:1.0.0-SNAPSHOT");
      assertNotNull(artifact);
      assertEquals(
            repository.resolve(
                  Paths.get("com", "ngc", "seaside", "test.project1", "1.0.0-SNAPSHOT",
                            "test.project1-1.0.0-SNAPSHOT.zip")).toAbsolutePath().toString(),
            artifact.toAbsolutePath().toString());
   }

   @Test
   public void testRepositoryServiceDependencies() {
      Set<Path>
            dependencies =
            service.getArtifactDependencies("com.ngc.seaside:test.project1:zip:1.0.0-SNAPSHOT", false);
      assertEquals(dependencies.toString(), 1, dependencies.size());
      Path dependency = dependencies.iterator().next();
      assertEquals(
            repository.resolve(
                  Paths.get("com", "ngc", "seaside", "test.project2", "2.0.0", "test.project2-2.0.0.zip"))
                  .toAbsolutePath().toString(),
            dependency.toAbsolutePath().toString());
   }

   @Test
   public void testRepositoryServiceTransitiveDependencies() {
      Set<Path>
            dependencies =
            service.getArtifactDependencies("com.ngc.seaside:test.project1:zip:1.0.0-SNAPSHOT", true);
      assertEquals(dependencies.toString(), 2, dependencies.size());
      Path[] dependencyArray = dependencies.toArray(new Path[2]);
      Path dependency1 = dependencyArray[0];
      Path dependency2 = dependencyArray[1];
      if (dependency1.toString().contains("test.project3")) {
         Path temp = dependency1;
         dependency1 = dependency2;
         dependency2 = temp;
      }
      assertEquals(
            repository.resolve(
                  Paths.get("com", "ngc", "seaside", "test.project2", "2.0.0", "test.project2-2.0.0.zip"))
                  .toAbsolutePath().toString(),
            dependency1.toAbsolutePath().toString());
      assertEquals(
            repository.resolve(
                  Paths.get("com", "ngc", "seaside", "test.project3", "3.0.0", "test.project3-3.0.0.zip"))
                  .toAbsolutePath().toString(),
            dependency2.toAbsolutePath().toString());
   }

}
