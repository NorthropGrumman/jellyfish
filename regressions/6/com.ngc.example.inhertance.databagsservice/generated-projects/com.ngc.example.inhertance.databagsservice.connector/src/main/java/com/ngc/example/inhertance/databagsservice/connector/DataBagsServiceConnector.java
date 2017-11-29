package com.ngc.example.inhertance.databagsservice.connector;

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
public class DataBagsServiceConnector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      transportService.addReceiver(this::receiveB,
         com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.B);

      eventService.addSubscriber(this::sendC,
         com.ngc.example.inhertance.databagsservice.event.datatype.C.TOPIC);

      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
            transportService.removeReceiver(this::receiveB,
         com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.B);

      eventService.removeSubscriber(this::sendC,
         com.ngc.example.inhertance.databagsservice.event.datatype.C.TOPIC);

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

   private Future<Collection<ITransportObject>> receiveB(ITransportObject transportObject, com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics transportTopic) {
      preReceiveMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.B);
      try {
         com.ngc.example.inhertance.databagsservice.datatype.B from;
         try {
            from = com.ngc.example.inhertance.databagsservice.datatype.B.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(DataBagsServiceDataConversion.convert(from), com.ngc.example.inhertance.databagsservice.event.datatype.B.TOPIC);
      } finally {
         postReceiveMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.B);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private void sendC(IEvent<com.ngc.example.inhertance.databagsservice.event.datatype.C> event) {
      preSendMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.C);
      com.ngc.example.inhertance.databagsservice.event.datatype.C from = event.getSource();
      com.ngc.example.inhertance.databagsservice.datatype.C to = DataBagsServiceDataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.C);
      } finally {
         postSendMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics.C);
      }
   }

   private void preReceiveMessage(ITransportTopic transportTopic) {
      ServiceRequest<ITransportTopic> request = new ServiceRequest<>(getRequirementsForTransportTopic(transportTopic),
                                                                     transportTopic);
      RequestThreadLocal.setCurrentRequest(request);
      logService.debug(getClass(), "Request begins.");
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics transportTopic) {
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

      switch((com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics) transportTopic){
         case B:
            requirements = Arrays.asList();
            break;
         case C:
            requirements = Arrays.asList();
            break;
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
