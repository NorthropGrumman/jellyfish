package com.test.model.connector;

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
public class ModelConnector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
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

   private void preReceiveMessage(ITransportTopic transportTopic) {
      ServiceRequest<ITransportTopic> request = new ServiceRequest<>(getRequirementsForTransportTopic(transportTopic),
                                                                     transportTopic);
      RequestThreadLocal.setCurrentRequest(request);
      logService.debug(getClass(), "Request begins.");
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(com.test.model.transport.topic.ModelTransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(com.test.model.transport.topic.ModelTransportTopics transportTopic) {
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

      switch((com.test.model.transport.topic.ModelTransportTopics) transportTopic){
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
