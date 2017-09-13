package ${dto.packageName};

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.monitoring.api.SessionlessRequirementAwareRequest;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.api.ITransportTopic;
import ${dto.basePackage}.transport.topic.${dto.model.name}TransportTopics;

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
public class ${dto.model.name}Connector {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
#foreach($inputEntry in $dto.inputTopics.entrySet())
#set ($topic = $inputEntry.key)
#set ($input = $inputEntry.value)
      transportService.addReceiver(this::receive${input.name}, 
         ${dto.model.name}TransportTopics.${topic});

#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($output = $outputEntry.value)
      eventService.addSubscriber(this::send${output.name}, 
         ${dto.basePackage}.events.${output.name}.TOPIC);

#end
      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
#foreach($inputEntry in $dto.inputTopics.entrySet())
#set ($topic = $inputEntry.key)
#set ($input = $inputEntry.value)
      transportService.removeReceiver(this::receive${input.name},
         ${dto.model.name}TransportTopics.${topic});

#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($output = $outputEntry.value)
      eventService.removeSubscriber(this::send${output.name},
         ${dto.basePackage}.events.${output.name}.TOPIC);

#end
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
#foreach($inputEntry in $dto.inputTopics.entrySet())
#set ($topic = $inputEntry.key)
#set ($input = $inputEntry.value)

   private void receive${input.name}(ITransportObject transportObject, ${dto.model.name}TransportTopics transportTopic) {
      preReceiveMessage(${dto.model.name}TransportTopics.${topic});
      try {
         ${input.fullyQualifiedName}Wrapper.${input.name} from;
         try {
            from =
               ${input.fullyQualifiedName}Wrapper.${input.name}.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(${dto.model.name}DataConversion.convert(from), ${dto.basePackage}.events.${input.name}.TOPIC);
      } finally {
         postReceiveMessage(${dto.model.name}TransportTopics.${topic});
      }
   }
#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($topic = $outputEntry.key)
#set ($output = $outputEntry.value)

   private void send${output.name}(IEvent<${dto.basePackage}.events.${output.name}> event) {
      preSendMessage(${dto.model.name}TransportTopics.${topic});
      ${dto.basePackage}.events.${output.name} from = event.getSource();
      ${output.fullyQualifiedName}Wrapper.${output.name} to = ${dto.model.name}DataConversion}.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()), 
            ${dto.model.name}TransportTopics.${topic});
      } finally {
         postSendMessage(${dto.model.name}TransportTopics.${topic});
      }
   }
#end

   private void preReceiveMessage(ITransportTopic transportTopic) {
      RequestThreadLocal.setCurrentRequest(new SessionlessRequirementAwareRequest(
         getRequirementsForTransportTopic(transportTopic)));
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(${dto.model.name}TransportTopics transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(${dto.model.name}TransportTopics transportTopic) {
      // Do nothing.
   }

   private void postSendMessage(ITransportTopic transportTopic) {
      logService.debug(getClass(), "Sent message on transport application topic %s.", transportTopic);
      RequestThreadLocal.clear();
   }

   private static Collection<String> getRequirementsForTransportTopic(ITransportTopic transportTopic) {
      final Collection<String> requirements;

      switch((${dto.model.name}TransportTopics) transportTopic){
#foreach($entry in $dto.topicRequirements.entrySet())
#set ($topic = $entry.key)
#set ($requirements = $entry.value)
#set ($size = $requirements.size())
         case ${topic}:
            requirements = Arrays.asList(#foreach($req in $requirements)"${req}"#if($velocityCount == $size)#{else}, #{end}#{end});
            break;
#end
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
