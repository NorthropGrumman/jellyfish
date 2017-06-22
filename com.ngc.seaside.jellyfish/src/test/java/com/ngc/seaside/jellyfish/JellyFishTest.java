package com.ngc.seaside.jellyfish;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

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
      System.setProperty("NG_FW_HOME", "C:\\projects\\ceacide\\jellyfish-cli\\com.ngc.seaside.jellyfish\\build\\resources\\main");
      JellyFish.main(new String[]{"root", "C:\\projects\\ceacide\\jellyfish-systemdescriptor-ext\\com.ngc.seaside.systemdescriptor.service.impl.xtext\\src\\test\\resources\\valid-project"});
   }

   @After
   public void after() throws Throwable {
     // component.deactivate();
   }
}
