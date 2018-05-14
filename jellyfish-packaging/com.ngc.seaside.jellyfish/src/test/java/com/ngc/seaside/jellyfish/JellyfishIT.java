package com.ngc.seaside.jellyfish;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JellyfishIT {

   @Before
   public void setup() {
      String ngFwHome = Paths.get(System.getProperty("user.dir"),
                                  "build",
                                  "resources",
                                  "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
   }

   @Test
   public void validSDProjectParsedTest() {
      Jellyfish.main(new String[]{"help"});
      Jellyfish.main(new String[]{"help", "-Dverbose=true"});
   }

   @After
   public void after() {
      System.clearProperty("NG_FW_HOME");
   }
}
