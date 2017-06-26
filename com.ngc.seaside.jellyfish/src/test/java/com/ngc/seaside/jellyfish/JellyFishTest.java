package com.ngc.seaside.jellyfish;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishTest
{

   private String previousNgFwHome;

   @Before
   public void before() throws Throwable
   {
      previousNgFwHome = System.getProperty("NG_FW_HOME");
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
   }

   @Test
   public void validSDProjectParsedTest() throws URISyntaxException
   {
      Path root = Paths.get(getClass().getClassLoader().getResource("valid-project").toURI());
      JellyFish.main(new String[] { "help", "-DinputDir=" + root });
   }
   
   @Test
   public void validateTest() throws URISyntaxException
   {
      Path root = Paths.get(getClass().getClassLoader().getResource("valid-project").toURI());
      JellyFish.main(new String[] { "validate", "-DinputDir=" + root });
   }

   @Test(expected = ParsingException.class)
   public void invalidSdProjectStructureParsed() throws URISyntaxException
   {
      Path root = Paths.get(getClass().getClassLoader().getResource("invalid-grammar-project").toURI());
      JellyFish.main(new String[] { "mock", "-DinputDir=" + root });
   }

   @Test(expected = IllegalArgumentException.class)
   public void invalidDirProjectStructureParsed() throws URISyntaxException
   {
      Path root = Paths.get(System.getProperty("user.dir"), "invalid", "path");
      JellyFish.main(new String[] { "mock", "-DinputDir=" + root });
   }

   @After
   public void after() throws Throwable
   {
      if (previousNgFwHome == null) {
         System.clearProperty("NG_FW_HOME");
      } else {
         System.setProperty("NG_FW_HOME", previousNgFwHome);
      }
   }
}
