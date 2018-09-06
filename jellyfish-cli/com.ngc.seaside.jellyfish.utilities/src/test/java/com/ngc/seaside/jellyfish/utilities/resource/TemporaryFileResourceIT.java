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
package com.ngc.seaside.jellyfish.utilities.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TemporaryFileResourceIT {

   private ITemporaryFileResource resource;

   private URL resourceUrl;

   private InputStream inputStream;

   @Before
   public void setup() throws Throwable {
      resourceUrl = TemporaryFileResourceIT.class.getClassLoader().getResource("testResource.txt");
      assertNotNull("failed to open resource for testing!",
                    resourceUrl);
      inputStream = resourceUrl.openStream();
   }

   @Test
   public void testDoesReadResourceFromJar() throws Throwable {
      resource = TemporaryFileResource.forClasspathResource(TemporaryFileResourceIT.class, "testResource.txt");
      assertEquals("url not correct!",
                   resourceUrl,
                   resource.getURL());
      resource.read(inputStream);

      Path tempFile = resource.getTemporaryFile();
      assertNotNull("tempFile not created!",
                    tempFile);
      assertEquals("fileName is not correct!",
                   "testResource.txt",
                   tempFile.getFileName().toString());
   }

   @After
   public void teardown() throws Throwable {
      if (inputStream != null) {
         inputStream.close();
      }
   }
}
