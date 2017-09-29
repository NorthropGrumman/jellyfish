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
         ${dto.transportTopicsClass}.${topic});

#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($output = $outputEntry.value)
      eventService.addSubscriber(this::send${output.name},
         ${dto.getEventsPackageName().apply($output)}.${output.name}.TOPIC);

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
         ${dto.transportTopicsClass}.${topic});

#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($output = $outputEntry.value)
      eventService.removeSubscriber(this::send${output.name},
         ${dto.getEventsPackageName().apply($output)}.${output.name}.TOPIC);

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
#set ($eventsPackage = $dto.getEventsPackageName().apply($input))
#set ($messagesPackage = $dto.getMessagesPackageName().apply($input))

   private Future<Collection<ITransportObject>> receive${input.name}(ITransportObject transportObject, ${dto.transportTopicsClass} transportTopic) {
      preReceiveMessage(${dto.transportTopicsClass}.${topic});
      try {
         ${messagesPackage}.${input.name} from;
         try {
            from = ${messagesPackage}.${input.name}.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(${dto.model.name}DataConversion.convert(from), ${eventsPackage}.${input.name}.TOPIC);
      } finally {
         postReceiveMessage(${dto.transportTopicsClass}.${topic});
      }
      return ITransportReceiver.EMPTY_RESPONSE;
   }
#end
#foreach($outputEntry in $dto.outputTopics.entrySet())
#set ($topic = $outputEntry.key)
#set ($output = $outputEntry.value)
#set ($eventsPackage = $dto.getEventsPackageName().apply($output))
#set ($messagesPackage = $dto.getMessagesPackageName().apply($output))

   private void send${output.name}(IEvent<${eventsPackage}.${output.name}> event) {
      preSendMessage(${dto.transportTopicsClass}.${topic});
      ${eventsPackage}.${output.name} from = event.getSource();
      ${messagesPackage}.${output.name} to = ${dto.model.name}DataConversion.convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
            ${dto.transportTopicsClass}.${topic});
      } finally {
         postSendMessage(${dto.transportTopicsClass}.${topic});
      }
   }
#end

   private void preReceiveMessage(ITransportTopic transportTopic) {
      RequestThreadLocal.setCurrentRequest(new SessionlessRequirementAwareRequest<>(
         getRequirementsForTransportTopic(transportTopic), this));
      logService.debug(getClass(), "Received message on transport application topic %s.", transportTopic);
   }

   private void postReceiveMessage(${dto.transportTopicsClass} transportTopic) {
      RequestThreadLocal.clear();
   }

   private void preSendMessage(${dto.transportTopicsClass} transportTopic) {
      // Do nothing.
   }

   private void postSendMessage(ITransportTopic transportTopic) {
      logService.debug(getClass(), "Sent message on transport application topic %s.", transportTopic);
      RequestThreadLocal.clear();
   }

   private static Collection<String> getRequirementsForTransportTopic(ITransportTopic transportTopic) {
      final Collection<String> requirements;

      switch((${dto.transportTopicsClass}) transportTopic){
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
