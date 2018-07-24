package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.artifact;
import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.currentJellyfishVersion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildManagementServiceTest {

   private BuildManagementService service;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IProjectInformation project;

   @Before
   public void setup() {
      DependenciesConfiguration config = new DependenciesConfiguration();
      config.addGroup()
            .versionPropertyName("blocsCoreVersion")
            .version("2.2.0")
            .defaultGroupId("com.ngc.blocs")
            .defaultScope(DependencyScope.BUILD)
            .includes(artifact("api"),
                      artifact("service.api"));
      config.addGroup()
            .versionPropertyName("gradlePluginsVersion")
            .version(currentJellyfishVersion())
            .defaultGroupId("com.ngc.seaside")
            .includes(artifact("gradle.plugins").scope(DependencyScope.BUILDSCRIPT));

      service = new BuildManagementService();
      service.setLogService(logService);
      service.setDependenciesConfiguration(config);
      service.activate();
   }

   @Test
   public void testDoesGetDependencyByGroupAndArtifact() {
      IBuildDependency dependency = service.getDependency(options, "com.ngc.blocs", "api");
      assertEquals("group ID not correct!",
                   "com.ngc.blocs",
                   dependency.getGroupId());
      assertEquals("artifact ID not correct!",
                   "api",
                   dependency.getArtifactId());
      assertEquals("version not correct!",
                   "2.2.0",
                   dependency.getVersion());
      assertEquals("version property name not correct!",
                   "blocsCoreVersion",
                   dependency.getVersionPropertyName());

      dependency = service.getDependency(options, "com.ngc.blocs", "service.api");
      assertEquals("group ID not correct!",
                   "com.ngc.blocs",
                   dependency.getGroupId());
      assertEquals("artifact ID not correct!",
                   "service.api",
                   dependency.getArtifactId());
      assertEquals("version not correct!",
                   "2.2.0",
                   dependency.getVersion());
      assertEquals("version property name not correct!",
                   "blocsCoreVersion",
                   dependency.getVersionPropertyName());
   }

   @Test
   public void testDoesGetDependencyByGroupAndArtifactAsSingleString() {
      IBuildDependency dependency = service.getDependency(options, "com.ngc.blocs:api");
      assertEquals("group ID not correct!",
                   "com.ngc.blocs",
                   dependency.getGroupId());
      assertEquals("artifact ID not correct!",
                   "api",
                   dependency.getArtifactId());
      assertEquals("version not correct!",
                   "2.2.0",
                   dependency.getVersion());
      assertEquals("version property name not correct!",
                   "blocsCoreVersion",
                   dependency.getVersionPropertyName());
   }

   @Test
   public void testDoesRegisterDependency() {
      IBuildDependency dependency = service.registerDependency(options, "com.ngc.blocs", "api");
      assertTrue("did not register build dependency!",
                 service.getRegisteredDependencies(options, DependencyScope.BUILD).contains(dependency));

      dependency = service.registerDependency(options, "com.ngc.seaside", "gradle.plugins");
      assertTrue("did not register buildscript dependency!",
                 service.getRegisteredDependencies(options, DependencyScope.BUILDSCRIPT).contains(dependency));
   }

   @Test
   public void testDoesRegisterProjects() {
      when(project.getDirectoryName()).thenReturn("generated-projects/artifactId");
      service.registerProject(options, project);

      Collection<IProjectInformation> projects = service.getRegisteredProjects();
      assertEquals("did not return registered projects!",
                   1,
                   projects.size());
      assertEquals("did not register project!",
                   project,
                   projects.iterator().next());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesThrowExceptionIfDependencyNotFound() {
      service.getDependency(options, "com.ngc.blocs", "foo");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesRejectInvocationWithStringThatIsNotAGroupAndArtifactId() {
      service.getDependency(options, "com.ngc.blocs@api");
   }
}
