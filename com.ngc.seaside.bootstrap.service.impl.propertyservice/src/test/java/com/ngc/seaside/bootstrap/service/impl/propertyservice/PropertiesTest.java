package com.ngc.seaside.bootstrap.service.impl.propertyservice;

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
