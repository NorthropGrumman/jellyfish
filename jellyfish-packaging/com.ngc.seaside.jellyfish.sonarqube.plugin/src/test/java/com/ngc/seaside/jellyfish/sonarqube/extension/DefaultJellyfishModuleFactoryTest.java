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
