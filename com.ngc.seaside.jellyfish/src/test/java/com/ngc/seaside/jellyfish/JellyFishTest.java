package com.ngc.seaside.jellyfish;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishTest {

   private JellyFish component;
   private ILogService logService;

   @Before
   public void before() throws Throwable {
      logService = new PrintStreamLogService();

//      component = new JellyFish();
//      component.setLogService(logService);
//      component.activate();
   }

   @Test
   public void doesRunAsACommandLineInterface() {
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test")
            .toAbsolutePath()
            .toString();
      String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "valid-project")
            .toAbsolutePath()
            .toString();

      System.setProperty("NG_FW_HOME", ngFwHome);
      JellyFish.main(new String[]{"-Droot=" + root});
   }

   @After
   public void after() throws Throwable {
      // component.deactivate();
   }
}
