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
