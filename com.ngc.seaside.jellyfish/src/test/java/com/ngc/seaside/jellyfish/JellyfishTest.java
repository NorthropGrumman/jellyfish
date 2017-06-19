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
public class JellyfishTest {

   private Jellyfish component;
   private ILogService logService;

   @Before
   public void before() throws Throwable {
      logService = new PrintStreamLogService();
   
      component = new Jellyfish();
      component.setLogService(logService);
      component.activate();
   }

   @Test
   public void doesSomething() {
     
   }

   @After
   public void after() throws Throwable {
      component.deactivate();
   }
}
