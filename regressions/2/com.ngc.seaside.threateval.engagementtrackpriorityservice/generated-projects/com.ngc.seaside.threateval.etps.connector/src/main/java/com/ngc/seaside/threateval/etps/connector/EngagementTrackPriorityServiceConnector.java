package com.ngc.seaside.threateval.etps.connector;

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.IRequest;
import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.requestmodel.api.Requests;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.time.api.Time;
import com.ngc.seaside.request.api.ServiceRequest;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.api.ITransportTopic;
import com.ngc.seaside.service.transport.api.ITransportReceiver;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Future;

@Component
public class EngagementTrackPriorityServiceConnector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      transportService.addReceiver(this::receiveTrackEngagementStatus,
         com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);

      eventService.addSubscriber(this::sendTrackPriority,
         com.ngc.seaside.threateval.etps.event.datatype.TrackPriority.TOPIC);

      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
            transportService.removeReceiver(this::receiveTrackEngagementStatus,
         com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);

      eventService.removeSubscriber(this::sendTrackPriority,
         com.ngc.seaside.threateval.etps.event.datatype.TrackPriority.TOPIC);

      logService.debug(getClass(), "Deactivated.");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeEventService")
   public void setEventService(IEventService ref) {
      this.eventService = ref;
   }

   public void removeEventService(IEventService ref) {
      setEventService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTransportService")
   public void setTransportService(ITransportService ref) {
      this.transportService = ref;
   }

   public void removeTransportService(ITransportService ref) {
      setTransportService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   private Future<Collection<ITransportObject>> receiveTrackEngagementStatus(ITransportObject transportObject, com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics transportTopic) {
      preReceiveMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      try {
         com.ngc.seaside.threateval.etps.engagementplanning.datatype.TrackEngagementStatus from;
         try {
            from = com.ngc.seaside.threateval.etps.engagementplanning.datatype.TrackEngagementStatus.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(EngagementTrackPriorityServiceDataConversion.convert(from), com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus.TOPIC);
      } finally {
         postReceiveMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_ENGAGEMENT_STATUS);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private void sendTrackPriority(IEvent<com.ngc.seaside.threateval.etps.event.datatype.TrackPriority> event) {
      preSendMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      com.ngc.seaside.threateval.etps.event.datatype.TrackPriority from = event.getSource();
      com.ngc.seaside.threateval.etps.datatype.TrackPriority to = EngagementTrackPriorityServiceDataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      } finally {
         postSendMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      }
   }

   private void preReceiveMessage(ITransportTopic transportTopic) {
      ServiceRequest<ITransportTopic> request = new ServiceRequest<>(getRequirementsForTransportTopic(transportTopic),
                                                                     transportTopic);
      RequestThreadLocal.setCurrentRequest(request);
      logService.debug(getClass(), "Request begins.");
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics transportTopic) {
      // Do nothing.
   }

   private void postSendMessage(ITransportTopic transportTopic) {
      logService.debug(getClass(), "Sent message on transport application topic %s.", transportTopic);
      IRequest request = Requests.getCurrentRequest();
      if(request != null) {
         Time now = Time.getCurrentTime();
         logService.debug(getClass(), "Request ends in %d ms.", now.subtract(request.getCreationTime()).getLongMsec());
      } else {
         logService.debug(getClass(), "Request ends.");
      }
      RequestThreadLocal.clear();
   }

   private static Collection<String> getRequirementsForTransportTopic(ITransportTopic transportTopic) {
      final Collection<String> requirements;

      switch((com.ngc.seaside.threateval.etps.transport.topic.EngagementTrackPriorityServiceTransportTopics) transportTopic){
         case TRACK_ENGAGEMENT_STATUS:
            requirements = Arrays.asList("TE001.2", "TE001.4");
            break;
         case TRACK_PRIORITY:
            requirements = Arrays.asList("TE001.2", "TE001.4");
            break;
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
