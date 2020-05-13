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
