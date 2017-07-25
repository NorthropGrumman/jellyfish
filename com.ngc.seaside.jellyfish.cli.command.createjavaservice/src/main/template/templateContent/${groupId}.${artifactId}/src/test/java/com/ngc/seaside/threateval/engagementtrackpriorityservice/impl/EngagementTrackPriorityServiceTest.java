package com.ngc.seaside.threateval.engagementtrackpriorityservice.impl;

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.ServiceInputFaultException;
import com.ngc.seaside.threateval.engagementplanning.events.TrackEngagementStatus;
import com.ngc.seaside.threateval.events.TrackPriority;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class EngagementTrackPriorityServiceTest {

   private EngagementTrackPriorityService service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void before() throws Throwable {
      service = new EngagementTrackPriorityService();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

   @Test
   public void doesCalculateTrackPriority() throws Throwable {
      TrackPriority priority = service.calculateTrackPriority(new TrackEngagementStatus()
                                                                    .setTrackId(1)
                                                                    .setPlannedEngagementCount(10)
                                                                    .setProbabilityOfKill(0.5f));
      assertEquals("priority not correct!",
                   new TrackPriority()
                         .setTrackId(1)
                         .setPriority(0.5f)
                         .setSourceId(EngagementTrackPriorityService.NAME),
                   priority);

      priority = service.calculateTrackPriority(new TrackEngagementStatus()
                                                      .setTrackId(1)
                                                      .setPlannedEngagementCount(10)
                                                      .setProbabilityOfKill(1.0f));
      assertEquals("priority not correct!",
                   new TrackPriority()
                         .setTrackId(1)
                         .setPriority(0.0f)
                         .setSourceId(EngagementTrackPriorityService.NAME),
                   priority);

      priority = service.calculateTrackPriority(new TrackEngagementStatus()
                                                      .setTrackId(1)
                                                      .setPlannedEngagementCount(10)
                                                      .setProbabilityOfKill(0.9f));
      assertEquals("priority not correct!",
                   new TrackPriority()
                         .setTrackId(1)
                         .setPriority(1.0f - 0.9f) // Avoid having to capture and use Float.compare().
                         .setSourceId(EngagementTrackPriorityService.NAME),
                   priority);
   }

   @Test
   public void doesThrowFaultIfPkIsInvalid() throws Throwable {
      try {
         service.calculateTrackPriority(new TrackEngagementStatus()
                                              .setTrackId(1)
                                              .setPlannedEngagementCount(1)
                                              .setProbabilityOfKill(-1));
         fail("expected ServiceInputFaultException!");
      } catch (ServiceInputFaultException e) {
         // Expected.
      }

      try {
         service.calculateTrackPriority(new TrackEngagementStatus()
                                              .setTrackId(1)
                                              .setPlannedEngagementCount(1)
                                              .setProbabilityOfKill(1.1f));
         fail("expected ServiceInputFaultException!");
      } catch (ServiceInputFaultException e) {
         // Expected.
      }
   }

   @After
   public void after() throws Throwable {
      service.deactivate();
   }
}
