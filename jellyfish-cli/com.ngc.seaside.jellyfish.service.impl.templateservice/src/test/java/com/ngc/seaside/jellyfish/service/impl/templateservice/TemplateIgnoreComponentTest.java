package com.ngc.seaside.jellyfish.service.impl.templateservice;

import static junit.framework.TestCase.assertEquals;

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
      assertEquals("[${groupId}.${artifactId}]/src/main/template/templateContent/${groupId}.${artifactId}", keys.get(1));
   }

}
