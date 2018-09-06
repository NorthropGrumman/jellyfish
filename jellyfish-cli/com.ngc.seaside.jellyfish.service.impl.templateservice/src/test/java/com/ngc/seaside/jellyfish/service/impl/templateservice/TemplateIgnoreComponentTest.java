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
package com.ngc.seaside.jellyfish.service.impl.templateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 *
 */
public class TemplateIgnoreComponentTest {

   private ILogService logService;
   private TemplateIgnoreComponent fixture;

   @Before
   public void setup() throws URISyntaxException {
      URL ignoreComponentResourceURL = getClass().getClassLoader().getResource("ignoreComponentResource");

      Path templateDir = Paths.get(ignoreComponentResourceURL.toURI());
      Path template = templateDir.resolve("templateContent");

      logService = new PrintStreamLogService();
      fixture = new TemplateIgnoreComponent(template, "templateContent", logService);
   }

   @Test
   public void testParse() throws IOException {

      fixture.parse();

      List<String> keys = fixture.getAllKeys();

      assertEquals("${groupId}.${artifactId}/src/main/template/template.ignore", keys.get(0));
      assertEquals("[${groupId}.${artifactId}]/src/main/template/templateContent/${groupId}.${artifactId}",
                   keys.get(1));
   }

}
