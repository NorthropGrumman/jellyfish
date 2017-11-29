package com.model.connector;

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
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
public class ModelConnector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      transportService.addReceiver(this::receiveData1,
         com.model.transport.topic.ModelTransportTopics.DATA1);

      transportService.addReceiver(this::receiveData2,
         com.model.transport.topic.ModelTransportTopics.DATA2);

      transportService.addReceiver(this::receiveData3,
         com.model.transport.topic.ModelTransportTopics.DATA3);

      eventService.addSubscriber(this::sendData4,
         com.model.event.Data4.TOPIC);

      eventService.addSubscriber(this::sendData5,
         com.model.event.Data5.TOPIC);

      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
            transportService.removeReceiver(this::receiveData1,
         com.model.transport.topic.ModelTransportTopics.DATA1);

      transportService.removeReceiver(this::receiveData2,
         com.model.transport.topic.ModelTransportTopics.DATA2);

      transportService.removeReceiver(this::receiveData3,
         com.model.transport.topic.ModelTransportTopics.DATA3);

      eventService.removeSubscriber(this::sendData4,
         com.model.event.Data4.TOPIC);

      eventService.removeSubscriber(this::sendData5,
         com.model.event.Data5.TOPIC);

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

   private Future<Collection<ITransportObject>> receiveData1(ITransportObject transportObject, com.model.transport.topic.ModelTransportTopics transportTopic) {
      preReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA1);
      try {
         com.model.Data1 from;
         try {
            from = com.model.Data1.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(ModelDataConversion.convert(from), com.model.event.Data1.TOPIC);
      } finally {
         postReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA1);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private Future<Collection<ITransportObject>> receiveData2(ITransportObject transportObject, com.model.transport.topic.ModelTransportTopics transportTopic) {
      preReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA2);
      try {
         com.model.Data2 from;
         try {
            from = com.model.Data2.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(ModelDataConversion.convert(from), com.model.event.Data2.TOPIC);
      } finally {
         postReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA2);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private Future<Collection<ITransportObject>> receiveData3(ITransportObject transportObject, com.model.transport.topic.ModelTransportTopics transportTopic) {
      preReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA3);
      try {
         com.model.Data3 from;
         try {
            from = com.model.Data3.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(ModelDataConversion.convert(from), com.model.event.Data3.TOPIC);
      } finally {
         postReceiveMessage(com.model.transport.topic.ModelTransportTopics.DATA3);
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }

   private void sendData4(IEvent<com.model.event.Data4> event) {
      preSendMessage(com.model.transport.topic.ModelTransportTopics.DATA4);
      com.model.event.Data4 from = event.getSource();
      com.model.Data4 to = ModelDataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            com.model.transport.topic.ModelTransportTopics.DATA4);
      } finally {
         postSendMessage(com.model.transport.topic.ModelTransportTopics.DATA4);
      }
   }

   private void sendData5(IEvent<com.model.event.Data5> event) {
      preSendMessage(com.model.transport.topic.ModelTransportTopics.DATA5);
      com.model.event.Data5 from = event.getSource();
      com.model.Data5 to = ModelDataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            com.model.transport.topic.ModelTransportTopics.DATA5);
      } finally {
         postSendMessage(com.model.transport.topic.ModelTransportTopics.DATA5);
      }
   }

   private void preReceiveMessage(ITransportTopic transportTopic) {
      RequestThreadLocal.setCurrentRequest(new ServiceRequest<>(
         getRequirementsForTransportTopic(transportTopic), this));
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(com.model.transport.topic.ModelTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(com.model.transport.topic.ModelTransportTopics transportTopic) {
      // Do nothing.
   }

   private void postSendMessage(ITransportTopic transportTopic) {
      logService.debug(getClass(), "Sent message on transport application topic %s.", transportTopic);
      RequestThreadLocal.clear();
   }

   private static Collection<String> getRequirementsForTransportTopic(ITransportTopic transportTopic) {
      final Collection<String> requirements;

      switch((com.model.transport.topic.ModelTransportTopics) transportTopic){
         case DATA1:
            requirements = Arrays.asList();
            break;
         case DATA2:
            requirements = Arrays.asList();
            break;
         case DATA3:
            requirements = Arrays.asList();
            break;
         case DATA4:
            requirements = Arrays.asList();
            break;
         case DATA5:
            requirements = Arrays.asList();
            break;
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
