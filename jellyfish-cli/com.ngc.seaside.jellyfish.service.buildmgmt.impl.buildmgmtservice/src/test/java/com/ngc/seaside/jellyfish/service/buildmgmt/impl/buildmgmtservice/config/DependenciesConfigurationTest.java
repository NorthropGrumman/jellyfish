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
package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Properties;

import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.artifact;
import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.currentJellyfishVersion;
import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.propertyNamed;
import static org.junit.Assert.assertEquals;

public class DependenciesConfigurationTest {

   private DependenciesConfiguration config;

   @Before
   public void setup() {
      config = new DependenciesConfiguration();
   }

   @Test
   public void testDoesConfigure() {
      config.addGroup()
            .versionPropertyName("starfishVersion")
            .version("2.1.0")
            .includes(artifact("service.api")
                            .groupId("com.ngc.seaside")
                            .scope(DependencyScope.BUILDSCRIPT));

      assertEquals("create too many groups!",
                   1,
                   config.getGroups().size());
      DependenciesConfiguration.Group group = config.getGroups().iterator().next();
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   group.getVersionPropertyName());
      assertEquals("version not correct!",
                   "2.1.0",
                   group.getVersion());
      assertEquals("did not register correct number of artifacts!",
                   1,
                   group.getArtifacts().size());

      Iterator<DependenciesConfiguration.Artifact> i = group.getArtifacts().iterator();
      DependenciesConfiguration.Artifact artifact = i.next();
      assertEquals("artifactId not correct!",
                   "service.api",
                   artifact.getArtifactId());
      assertEquals("groupId not correct!",
                   "com.ngc.seaside",
                   artifact.getGroupId());
      assertEquals("version not correct!",
                   "2.1.0",
                   artifact.getVersion());
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   artifact.getVersionPropertyName());
      assertEquals("scope not correct!",
                   DependencyScope.BUILDSCRIPT,
                   artifact.getScope());
   }

   @Test
   public void testDoesConfigureWithDefaultValues() {
      config.addGroup()
            .versionPropertyName("starfishVersion")
            .version("2.1.0")
            .defaultGroupId("com.ngc.seaside")
            .defaultScope(DependencyScope.BUILD)
            .includes(artifact("service.api"),
                      artifact("service.transport.api"),
                      artifact("service.correlation.impl.correlationservice")
                            .groupId("com.ngc.override")
                            .scope(DependencyScope.BUILDSCRIPT));

      assertEquals("create too many groups!",
                   1,
                   config.getGroups().size());
      DependenciesConfiguration.Group group = config.getGroups().iterator().next();
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   group.getVersionPropertyName());
      assertEquals("version not correct!",
                   "2.1.0",
                   group.getVersion());
      assertEquals("did not register correct number of artifacts!",
                   3,
                   group.getArtifacts().size());

      Iterator<DependenciesConfiguration.Artifact> i = group.getArtifacts().iterator();
      DependenciesConfiguration.Artifact artifact = i.next();
      assertEquals("artifactId not correct!",
                   "service.api",
                   artifact.getArtifactId());
      assertEquals("groupId not correct!",
                   "com.ngc.seaside",
                   artifact.getGroupId());
      assertEquals("version not correct!",
                   "2.1.0",
                   artifact.getVersion());
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   artifact.getVersionPropertyName());
      assertEquals("scope not correct!",
                   DependencyScope.BUILD,
                   artifact.getScope());

      i.next();
      artifact = i.next();
      assertEquals("overridden groupId not correct!",
                   "com.ngc.override",
                   artifact.getGroupId());
      assertEquals("overridden scope not correct!",
                   DependencyScope.BUILDSCRIPT,
                   artifact.getScope());
   }

   @Test
   public void testDoesConfigureWithProperties() {
      config.addGroup()
            .versionPropertyName("starfishVersion")
            .version(currentJellyfishVersion())
            .defaultGroupId(propertyNamed("starfishGroupId"))
            .defaultScope(DependencyScope.BUILD)
            .includes(artifact(propertyNamed("starfishApi")));

      Properties properties = new Properties();
      properties.put("starfishGroupId", "com.ngc.seaside");
      properties.put("starfishApi", "service.api");
      properties.put(DependenciesConfiguration.CURRENT_JELLYFISH_VERSION_PROPERTY_NAME, "2.1.0");
      config.resolve(properties);

      assertEquals("create too many groups!",
                   1,
                   config.getGroups().size());
      DependenciesConfiguration.Group group = config.getGroups().iterator().next();
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   group.getVersionPropertyName());
      assertEquals("version not correct!",
                   "2.1.0",
                   group.getVersion());
      assertEquals("did not register correct number of artifacts!",
                   1,
                   group.getArtifacts().size());

      Iterator<DependenciesConfiguration.Artifact> i = group.getArtifacts().iterator();
      DependenciesConfiguration.Artifact artifact = i.next();
      assertEquals("artifactId not correct!",
                   "service.api",
                   artifact.getArtifactId());
      assertEquals("groupId not correct!",
                   "com.ngc.seaside",
                   artifact.getGroupId());
      assertEquals("version not correct!",
                   "2.1.0",
                   artifact.getVersion());
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   artifact.getVersionPropertyName());
      assertEquals("scope not correct!",
                   DependencyScope.BUILD,
                   artifact.getScope());
   }

   @Test(expected = IllegalStateException.class)
   public void testDoesFailValidationIfGroupIncomplete() {
      config.addGroup()
            .versionPropertyName("starfishVersion");
      config.validate();
   }

   @Test(expected = IllegalStateException.class)
   public void testDoesThrowErrorIfPropertyNotDefined() {
      config.addGroup()
            .versionPropertyName("starfishVersion")
            .version(currentJellyfishVersion())
            .defaultGroupId(propertyNamed("starfishGroupId"))
            .defaultScope(DependencyScope.BUILD)
            .includes(artifact(propertyNamed("starfishApi")));

      Properties properties = new Properties();
      properties.put("starfishVersionPropertyName", "starfishVersion");
      config.resolve(properties);

      assertEquals("create too many groups!",
                   1,
                   config.getGroups().size());
      DependenciesConfiguration.Group group = config.getGroups().iterator().next();
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   group.getVersionPropertyName());
      assertEquals("version not correct!",
                   "2.1.0",
                   group.getVersion());
      assertEquals("did not register correct number of artifacts!",
                   3,
                   group.getArtifacts().size());

      Iterator<DependenciesConfiguration.Artifact> i = group.getArtifacts().iterator();
      DependenciesConfiguration.Artifact artifact = i.next();
      assertEquals("artifactId not correct!",
                   "service.api",
                   artifact.getArtifactId());
      assertEquals("groupId not correct!",
                   "com.ngc.seaside",
                   artifact.getGroupId());
      assertEquals("version not correct!",
                   "2.1.0",
                   artifact.getVersion());
      assertEquals("versionPropertyName not correct!",
                   "starfishVersion",
                   artifact.getVersionPropertyName());
      assertEquals("scope not correct!",
                   DependencyScope.BUILD,
                   artifact.getScope());
   }
}
