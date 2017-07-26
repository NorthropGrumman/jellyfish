package com.ngc.seaside.threateval.engagementtrackpriorityservice.connector;

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.IEventSubscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.event.MockedEvents;
import com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatusWrapper;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportReceiver;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.threateval.datatype.TrackPriorityWrapper;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.transport.topic.EngagementTrackPriorityServiceTransportTopics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class EngagementTrackPriorityConnectorTest {

   private static final int ENGAGEMENT_COUNT = 10;
   private static final int TRACK_ID = 20;
   private static final float KILL_PROBABILITY = 0.75f;
   private static final float PRIORITY = 0.25f;
   private static final String SOURCE_ID = "mock_source";

   private EngagementTrackPriorityServiceConnector connector;

   @Mock
   private ITransportService transportService;

   @Mock
   private IEventService eventService;

   @Mock
   private ILogService logService = Mockito.mock(ILogService.class);

   private final TrackEngagementStatusWrapper.TrackEngagementStatus trackEngagementStatus =
         TrackEngagementStatusWrapper.TrackEngagementStatus.newBuilder()
               .setPlannedEngagementCount(ENGAGEMENT_COUNT)
               .setTrackId(TRACK_ID)
               .setProbabilityOfKill(KILL_PROBABILITY)
               .build();

   private final TrackPriority trackPriority = new TrackPriority()
         .setPriority(PRIORITY)
         .setSourceId(SOURCE_ID)
         .setTrackId(TRACK_ID);

   @Before
   public void before() throws Throwable {
      connector = new EngagementTrackPriorityServiceConnector();
      connector.setTransportService(transportService);
      connector.setEventService(eventService);
      connector.setLogService(logService);
      connector.activate();
   }

   @Test
   public void doesPublishToEventServiceWhenTransportObjectRecieved() throws Throwable {
      // Verify the connector subscribed to the transport topic and capture the receiver it registered.
      ArgumentCaptor<ITransportReceiver<EngagementTrackPriorityServiceTransportTopics>> captor =
            ArgumentCaptor.forClass(ITransportReceiver.class);
      verify(transportService).addReceiver(captor.capture(),
                                           eq(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS));

      ITransportObject object = ITransportObject.withPayload(trackEngagementStatus.toByteArray());
      captor.getValue().receive(object, EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);

      // Verify the correct event was published.
      verify(eventService).publish(new TrackEngagementStatus()
                                         .setTrackId(trackEngagementStatus.getTrackId())
                                         .setPlannedEngagementCount(trackEngagementStatus.getPlannedEngagementCount())
                                         .setProbabilityOfKill(trackEngagementStatus.getProbabilityOfKill()),
                                   TrackEngagementStatus.TOPIC);
   }

   @Test
   public void doesPublishToTransportServiceWhenEventReceived() throws Throwable {
      // Verify the connector subscribed to the event topic and capture the listener it registered.
      ArgumentCaptor<IEventSubscriber<TrackPriority>> captor =
            ArgumentCaptor.forClass(IEventSubscriber.class);
      verify(eventService).addSubscriber(captor.capture(),
                                         eq(TrackPriority.TOPIC));

      captor.getValue().eventReceived(MockedEvents.of(trackPriority, TrackPriority.TOPIC));

      // Verify the correct transport object was published.
      TrackPriorityWrapper.TrackPriority expected = TrackPriorityWrapper.TrackPriority.newBuilder()
            .setPriority(trackPriority.getPriority())
            .setTrackId(trackPriority.getTrackId())
            .setSourceId(trackPriority.getSourceId())
            .build();
      verify(transportService).send(eq(ITransportObject.withPayload(expected.toByteArray())),
                                    eq(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY));
   }

   @Test
   public void doesUnsubscribeWhenDeactivated() throws Throwable {
      connector.deactivate();
      verify(transportService).removeReceiver(any(ITransportReceiver.class),
                                              eq(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS));
      verify(eventService).removeSubscriber(any(IEventSubscriber.class),
                                            eq(TrackPriority.TOPIC));
   }

   @After
   public void after() throws Throwable {
      connector.deactivate();
   }
}
