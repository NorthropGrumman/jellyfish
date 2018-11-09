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
package com.ngc.seaside.jellyfish.sonarqube.extension;

import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.seaside.jellyfish.sonarqube.module.NoOpLogServiceModule;
import com.ngc.seaside.jellyfish.sonarqube.module.SonarqubeLogServiceModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJellyfishModuleFactoryTest {

   private DefaultJellyfishModuleFactory factory;

   @Mock
   private Module module;

   @Before
   public void setup() {
      factory = new DefaultJellyfishModuleFactory();
   }

   @Test
   public void testDoesIncludeConfiguredModules() {
      factory.addModules(module);
      assertTrue("did not include configured module!",
                 factory.getJellyfishModules(true).contains(module));
   }

   @Test
   public void testDoesConfigureLogging() {
      assertTrue("did not include logging module!",
                 factory.getJellyfishModules(true).stream().anyMatch(m -> m instanceof SonarqubeLogServiceModule));
      assertTrue("did not include noop logging module!",
                 factory.getJellyfishModules(false).stream().anyMatch(m -> m instanceof NoOpLogServiceModule));
   }

   @Test
   public void testDoesFilterOutLogServiceModule() {
      assertTrue("factory should exclude log service module!",
                 factory.shouldModuleBeExcluded(new LogServiceModule()));
      assertFalse("module should not be excluded!",
                  factory.shouldModuleBeExcluded(module));
   }
}
