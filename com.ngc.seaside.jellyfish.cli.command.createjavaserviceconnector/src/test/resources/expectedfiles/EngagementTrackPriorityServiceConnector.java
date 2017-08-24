package com.ngc.seaside.threateval.engagementtrackpriorityservice.connector;

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.monitoring.api.SessionlessRequirementAwareRequest;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.api.ITransportTopic;
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
      transportService.addReceiver(this::receiveTrackEngagementStatus, 
         EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);

      eventService.addSubscriber(this::sendTrackPriority, 
         com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority.TOPIC);

      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
      transportService.removeReceiver(this::receiveTrackEngagementStatus,
         EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);

      eventService.removeSubscriber(this::sendTrackPriority,
         com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority.TOPIC);

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

   private void receiveTrackEngagementStatus(ITransportObject transportObject,
                                          EngagementTrackPriorityServiceTransportTopics transportTopic) {
      preReceiveMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      try {
         com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatusWrapper.TrackEngagementStatus trackEngagementStatus;
         try {
            trackEngagementStatus =
               com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatusWrapper.TrackEngagementStatus.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(convert(trackEngagementStatus), com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus.TOPIC);
      } finally {
         postReceiveMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      }
   }

   private void sendTrackPriority(IEvent<com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority> event) {
      preSendMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority from = event.getSource();
      com.ngc.seaside.threateval.datatype.TrackPriorityWrapper.TrackPriority to = convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()), 
            EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      } finally {
         postSendMessage(EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      }
   }

   private static com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus convert(com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatusWrapper.TrackEngagementStatus from) {
      com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus to = new com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus();

      to.setTrackId(from.getTrackId());
      to.setPlannedEngagementCount(from.getPlannedEngagementCount());
      to.setProbabilityOfKill(from.getProbabilityOfKill());

      return to;
   }

   private static com.ngc.seaside.threateval.datatype.TrackPriorityWrapper.TrackPriority convert(com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority from) {
      com.ngc.seaside.threateval.datatype.TrackPriorityWrapper.TrackPriority.Builder to = com.ngc.seaside.threateval.datatype.TrackPriorityWrapper.TrackPriority.newBuilder();

      to.setTrackId(from.getTrackId());
      to.setSourceId(from.getSourceId());
      to.setPriority(from.getPriority());

      return to.build();
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
      final Collection<String> requirements;

      switch((EngagementTrackPriorityServiceTransportTopics) transportTopic){
         case TRACK_ENGAGEMENT_STATUS:
            requirements = Arrays.asList(
               "TE001.2",
               "TE001.4"
            );
            break;
         case TRACK_PRIORITY:
            requirements = Arrays.asList(
               "TE001.2",
               "TE001.4"
            );
            break;
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
