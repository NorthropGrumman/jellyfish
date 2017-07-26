package com.ngc.seaside.threateval.engagementtrackpriorityservice.connector;

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatusWrapper;
import com.ngc.seaside.service.monitoring.api.SessionlessRequirementAwareRequest;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.api.ITransportTopic;
import com.ngc.seaside.threateval.datatype.TrackPriorityWrapper;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.transport.topic.EngagementTrackPriorityServiceTransportTopics;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class EngagementTrackPriorityServiceConnector {

   private ILogService logService;

   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      eventService.addSubscriber(this::sendTrackPriority,
                                 TrackPriority.TOPIC);
      transportService.addReceiver(this::receiveTrackEngagementStatus,
                                   EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
      transportService.removeReceiver(this::receiveTrackEngagementStatus,
                                      EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      eventService.removeSubscriber(this::sendTrackPriority,
                                    TrackPriority.TOPIC);
      logService.debug(getClass(), "Deactivated.");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setEventService(IEventService ref) {
      this.eventService = ref;
   }

   public void removeEventService(IEventService ref) {
      setEventService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setTransportService(ITransportService ref) {
      this.transportService = ref;
   }

   public void removeTransportService(ITransportService ref) {
      setTransportService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   private void sendTrackPriority(IEvent<TrackPriority> event) {
      preSendMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      try {
         TrackPriority from = event.getSource();
         TrackPriorityWrapper.TrackPriority to = convert(from);
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
                               EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
         postSendMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      } finally {
         postSendMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      }
   }

   private void receiveTrackEngagementStatus(ITransportObject transportObject,
                                             EngagementTrackPriorityServiceTransportTopics transportTopic) {
      preReceiveMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      try {
         TrackEngagementStatusWrapper.TrackEngagementStatus trackEngagementStatus;
         try {
            trackEngagementStatus =
                  TrackEngagementStatusWrapper.TrackEngagementStatus.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(convert(trackEngagementStatus), TrackEngagementStatus.TOPIC);
      } finally {
         postReceiveMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      }
   }

   private TrackEngagementStatus convert(TrackEngagementStatusWrapper.TrackEngagementStatus from) {
      TrackEngagementStatus to = new TrackEngagementStatus();
      to.setPlannedEngagementCount(from.getPlannedEngagementCount());
      to.setProbabilityOfKill(from.getProbabilityOfKill());
      to.setTrackId(from.getTrackId());
      return to;
   }

   private TrackPriorityWrapper.TrackPriority convert(TrackPriority from) {
      return TrackPriorityWrapper.TrackPriority.newBuilder().setPriority(from.getPriority())
            .setSourceId(from.getSourceId()).setTrackId(from.getTrackId()).build();
   }

   private void preReceiveMessage(ITransportTopic transportTopic) {
      RequestThreadLocal.setCurrentRequest(new SessionlessRequirementAwareRequest(
            getRequirementsForTransportTopic(transportTopic)));
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(EngagementTrackPriorityServiceTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(EngagementTrackPriorityServiceTransportTopics transportTopic) {
      // Do nothing.
   }

   private void postSendMessage(ITransportTopic transportTopic) {
      logService.debug(getClass(), "Sent message on transport application topic %s.", transportTopic);
      RequestThreadLocal.clear();
   }

   private static Collection<String> getRequirementsForTransportTopic(ITransportTopic transportTopic) {
      Collection<String> requirements = Collections.emptyList();
      switch (transportTopic) {

         case EngagementTrackPriorityServiceTransportTopics.trackEngagementStatus:
            requirements = Arrays.asList(
                  "TE001.2",
                  "TE001.4",
                  );
            break;

         case EngagementTrackPriorityServiceTransportTopics.trackPriority:
            requirements = Arrays.asList(
                  "TE001.2",
                  "TE001.4",
                  );
            break;

         default:
            //do nothing
            break;
      }

      return requirements;
   }
}
