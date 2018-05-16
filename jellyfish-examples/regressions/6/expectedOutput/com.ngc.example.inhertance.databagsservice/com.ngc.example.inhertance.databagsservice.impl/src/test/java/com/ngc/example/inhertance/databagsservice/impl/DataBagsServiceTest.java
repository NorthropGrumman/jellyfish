package com.ngc.example.inhertance.databagsservice.impl;

import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class DataBagsServiceTest {

   private DataBagsService service;

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws Throwable {
      service = new DataBagsService();
      service.setLogService(logService);
      service.activate();
   }

   @Test
   public void doPublishTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
