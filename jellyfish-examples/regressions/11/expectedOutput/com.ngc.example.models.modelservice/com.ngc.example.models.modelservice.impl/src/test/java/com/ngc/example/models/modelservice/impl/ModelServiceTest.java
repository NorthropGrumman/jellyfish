package com.ngc.example.models.modelservice.impl;

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
public class ModelServiceTest {

   private ModelService service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new ModelService();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
