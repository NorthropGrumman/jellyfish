package com.ngc.seaside.threateval.tps.connector;

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
public class TrackPriorityServiceConnector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      transportService.addReceiver(this::receiveDroppedSystemTrack,
         com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.DROPPED_SYSTEM_TRACK);

      transportService.addReceiver(this::receiveTrackPriority,
         com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.TRACK_PRIORITY);

      eventService.addSubscriber(this::sendPrioritizedSystemTrackIdentifiers,
         com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers.TOPIC);

      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
            transportService.removeReceiver(this::receiveDroppedSystemTrack,
         com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.DROPPED_SYSTEM_TRACK);

      transportService.removeReceiver(this::receiveTrackPriority,
         com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.TRACK_PRIORITY);

      eventService.removeSubscriber(this::sendPrioritizedSystemTrackIdentifiers,
         com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers.TOPIC);

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

   private Future<Collection<ITransportObject>> receiveDroppedSystemTrack(ITransportObject transportObject, com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics transportTopic) {
      preReceiveMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.DROPPED_SYSTEM_TRACK);
      try {
         com.ngc.seaside.threateval.tps.common.datatype.DroppedSystemTrack from;
         try {
            from = com.ngc.seaside.threateval.tps.common.datatype.DroppedSystemTrack.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(TrackPriorityServiceDataConversion.convert(from), com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack.TOPIC);
      } finally {
         postReceiveMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.DROPPED_SYSTEM_TRACK);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private Future<Collection<ITransportObject>> receiveTrackPriority(ITransportObject transportObject, com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics transportTopic) {
      preReceiveMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      try {
         com.ngc.seaside.threateval.tps.datatype.TrackPriority from;
         try {
            from = com.ngc.seaside.threateval.tps.datatype.TrackPriority.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(TrackPriorityServiceDataConversion.convert(from), com.ngc.seaside.threateval.tps.event.datatype.TrackPriority.TOPIC);
      } finally {
         postReceiveMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.TRACK_PRIORITY);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private void sendPrioritizedSystemTrackIdentifiers(IEvent<com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers> event) {
      preSendMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.PRIORITIZED_SYSTEM_TRACK_IDENTIFIERS);
      com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers from = event.getSource();
      com.ngc.seaside.threateval.tps.datatype.PrioritizedSystemTrackIdentifiers to = TrackPriorityServiceDataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.PRIORITIZED_SYSTEM_TRACK_IDENTIFIERS);
      } finally {
         postSendMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics.PRIORITIZED_SYSTEM_TRACK_IDENTIFIERS);
      }
   }

   private void preReceiveMessage(ITransportTopic transportTopic) {
      ServiceRequest<ITransportTopic> request = new ServiceRequest<>(getRequirementsForTransportTopic(transportTopic),
                                                                     transportTopic);
      RequestThreadLocal.setCurrentRequest(request);
      logService.debug(getClass(), "Request begins.");
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics transportTopic) {
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

      switch((com.ngc.seaside.threateval.tps.transport.topic.TrackPriorityServiceTransportTopics) transportTopic){
         case DROPPED_SYSTEM_TRACK:
            requirements = Arrays.asList("TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5");
            break;
         case PRIORITIZED_SYSTEM_TRACK_IDENTIFIERS:
            requirements = Arrays.asList("TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5");
            break;
         case TRACK_PRIORITY:
            requirements = Arrays.asList("TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5");
            break;
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
