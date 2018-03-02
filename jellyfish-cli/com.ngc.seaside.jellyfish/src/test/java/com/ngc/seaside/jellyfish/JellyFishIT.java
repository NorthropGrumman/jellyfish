package com.ngc.seaside.jellyfish;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URISyntaxException;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
@Ignore("This test is hanging right now due to the SD project not being installed.  Need to figure this out.  Its string to download the system descriptor files for the Jellyfish project which doesn't exist.")
public class JellyFishIT {

   private String previousNgFwHome;

   @Before
   public void before() throws Throwable {
      previousNgFwHome = System.getProperty("NG_FW_HOME");
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
   }

   @Test
   public void validSDProjectParsedTest() throws URISyntaxException {
      JellyFish.main(new String[] { "help" });
      JellyFish.main(new String[] { "help", "-Dverbose=true" });
   }

   @After
   public void after() throws Throwable {
      if (previousNgFwHome == null) {
         System.clearProperty("NG_FW_HOME");
      } else {
         System.setProperty("NG_FW_HOME", previousNgFwHome);
      }
   }
}
