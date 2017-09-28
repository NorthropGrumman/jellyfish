package com.ngc.test.model1.impl;

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
public class Model1Test {

   private Model1 service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new Model1();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

   @Test
   public void pubsub1Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void duplicatePubsubTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void pubsub2Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void sink1Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void duplicateSinkTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void sink2Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void source1Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void duplicateSourceTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void source2Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }


   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
