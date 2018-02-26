package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.resource.MockedResourceService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BuildManagementServiceTest {

   private BuildManagementService service;

   private IResourceService resourceService;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() {
      resourceService = new MockedResourceService()
            .onNextReadDrain(getClass().getClassLoader().getResourceAsStream("dependencies.json"));

      service = new BuildManagementService();
      service.setLogService(logService);
      service.setResourceService(resourceService);
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
      assertTrue("did not register normal dependency!",
                 service.getRegisteredDependencies(options, DependencyScope.NORMAL).contains(dependency));

      dependency = service.registerDependency(options, "com.ngc.seaside", "gradle.plugins");
      assertTrue("did not register buildscript dependency!",
                 service.getRegisteredDependencies(options, DependencyScope.BUILDSCRIPT).contains(dependency));
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
