package com.ngc.blocs.json.resource.impl.common.json;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonResourceTest {

   private final static String VALID_EXAMPLE = "valid.json";
   private final static String INVALID_EXAMPLE = "invalid.json";

   private JsonResource<JsonTest> resource;

   @Rule
   public TemporaryFolder outputDirectory = new TemporaryFolder();

   @Test
   public void testDoesRead() {
      resource = new JsonResource<>(VALID_EXAMPLE,
                                    JsonTest.class);

      assertEquals("file not correct!",
                   VALID_EXAMPLE,
                   resource.getFile());
      assertTrue("should return true if successful!",
                 resource.read(getClass().getClassLoader().getResourceAsStream(VALID_EXAMPLE)));

      JsonTest test = resource.get();
      assertNotNull("did not get object from JSON!",
                    test);
      assertEquals("did not load fields correctly!",
                   "Hello World",
                   test.getMessage());
      assertNull("error should be null!",
                 resource.getError());
   }

   @Test
   public void testHandleReadFailure() {
      resource = new JsonResource<>(INVALID_EXAMPLE,
                                    JsonTest.class);

      assertFalse("should have return false if an error occurred!",
                  resource.read(getClass().getClassLoader().getResourceAsStream(INVALID_EXAMPLE)));
      assertNotNull("did not return error!",
                    resource.getError());
      assertNull("object should be null if read failed!",
                 resource.get());
   }

   @Test
   public void testDoesHandleTypeMismatch() {
      JsonResource<List> resource = new JsonResource<>(VALID_EXAMPLE,
                                                       List.class);
      assertFalse("should have return false if an error occurred!",
                  resource.read(getClass().getClassLoader().getResourceAsStream(VALID_EXAMPLE)));
      assertNotNull("did not return error!",
                    resource.getError());
      assertNull("object should be null if read failed!",
                 resource.get());
   }

   @Test
   public void testDoesWrite() throws Throwable {
      File file = outputDirectory.newFile();
      resource = new JsonResource<>(file.getPath(),
                                    JsonTest.class);
      JsonTest test = new JsonTest();
      test.setMessage("Hello World from a test");
      resource.set(test);

      assertTrue("should return true if successful!",
                 resource.write(new FileOutputStream(file)));
      assertNull("error should be null!",
                 resource.getError());
      assertTrue("file not created!",
                 file.exists());
   }

   public static class JsonTest {

      private String message;

      public String getMessage() {
         return message;
      }

      public void setMessage(String message) {
         this.message = message;
      }
   }
}
