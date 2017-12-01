package com.model.impl;

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

   private Model service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new Model();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

   @Test
   public void basicPubSubTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void basicSinkTest() throws Exception {
   // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void singleOutputCorrelationTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void multiOutputPubSubTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void complexPubSubTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void basicSourceTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void multiSourceTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
