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
package com.ngc.seaside.jellyfish.service.impl.jellyfishuserservice;

import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class JellyfishUserServiceTest {

   @Mock
   private ILogService logService;

   @Before
   public void setup() {
      System.setProperty(JellyfishUserService.JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE,
                         Paths.get("src", "test", "resources").toString());
   }

   @Test
   public void test() {

      JellyfishUserService service = new JellyfishUserService(logService);
      Map<String, String> properties = service.getJellyfishUserProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      assertEquals("bar", properties.get("foo"));
   }

   @After
   public void after() {
      System.clearProperty(JellyfishUserService.JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE);
   }
}
