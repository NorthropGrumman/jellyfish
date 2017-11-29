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
   public void basicPubSubTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void basicSinkTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void multiOutputPubSubTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void complexPubSubTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void basicSourceTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void multiSourceTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void sinkCorrelationTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void singleOutputCorrelationTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void multiOutputCorrelationTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void multiOutputCorrelationSameTypeTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void complexCorrelationTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

   @Test
   public void complexCorrelationSameTypeTest(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }


   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
