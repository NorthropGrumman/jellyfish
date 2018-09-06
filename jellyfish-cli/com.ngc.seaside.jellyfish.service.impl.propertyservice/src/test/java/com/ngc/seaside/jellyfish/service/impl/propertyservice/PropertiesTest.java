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
package com.ngc.seaside.jellyfish.service.impl.propertyservice;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 *
 */
public class PropertiesTest {

   private Properties fixture;

   @Test
   public void doesLoad() throws URISyntaxException, IOException {
      fixture = new Properties();
      URL propertiesURL = getClass().getClassLoader().getResource("dynamic.properties");
      assertNotNull(propertiesURL);
      File file = new File(propertiesURL.toURI());

      fixture.load(Paths.get(file.getAbsolutePath()));
      fixture.evaluate();

      assertEquals("MyClass", fixture.get("classname"));
      assertEquals("com.ngc.seaside", fixture.get("groupId"));
      assertEquals("myclass", fixture.get("artifactId"));
      assertEquals("com.ngc.seaside.myclass", fixture.get("package"));
      assertEquals("com-ngc-seaside-myclass", fixture.get("dashPackage"));
   }


   @Test
   public void doesReevaluate() throws URISyntaxException, IOException {
      fixture = new Properties();
      URL propertiesURL = getClass().getClassLoader().getResource("dynamic.properties");
      assertNotNull(propertiesURL);
      File file = new File(propertiesURL.toURI());

      fixture.load(Paths.get(file.getAbsolutePath()));
      fixture.evaluate();

      assertEquals("MyClass", fixture.get("classname"));
      assertEquals("my-class", fixture.get("hyphens"));
      assertEquals("com.ngc.seaside", fixture.get("groupId"));
      assertEquals("myclass", fixture.get("artifactId"));
      assertEquals("com.ngc.seaside.myclass", fixture.get("package"));
      assertEquals("com-ngc-seaside-myclass", fixture.get("dashPackage"));

      fixture.put("classname", "ADiffClass");
      fixture.evaluate();

      assertEquals("ADiffClass", fixture.get("classname"));
      assertEquals("adiffclass", fixture.get("artifactId"));
   }

}
