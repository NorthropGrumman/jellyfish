package ${package};

import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.blocs.requestmodel.api.RequestThreadLocal;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.monitoring.api.SessionlessRequirementAwareRequest;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.api.ITransportTopic;
#foreach($field in $model.getInputs())
import ${field.getType().getFullyQualifiedName()}Wrapper;
#end
#foreach($field in $model.getOutputs())
import ${field.getType().getFullyQualifiedName()}Wrapper;
#end
#foreach($field in $model.getInputs())
import ${model.getParent().getName()}.${model.getName().toLowerCase()}.events.${field.getType().getName()};
#end
#foreach($field in $model.getOutputs())
import ${model.getParent().getName()}.${model.getName().toLowerCase()}.events.${field.getType().getName()};
#end
import ${model.getParent().getName()}.${model.getName().toLowerCase()}.transport.topic.${model.getName()}TransportTopics;

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
public class ${javaClassName} {

   private ILogService logService;
   private IEventService eventService;
   private ITransportService transportService;

   @SuppressWarnings("unchecked")
   @Activate
   public void activate() {
      #foreach($field in $model.getInputs())
      #set( $className = $field.getType().getName() )
      transportService.addReceiver(this::receive${className},
                                      ${model.getName()}TransportTopics.TRACK_ENGAGEMENT_STATUS); //TODO address .track_engagement_status

      #end
      #foreach($field in $model.getOutputs())
      #set( $className = $field.getType().getName() )
      eventService.addSubscriber(this::send${className},
                                    ${className}.TOPIC); //TODO address .TOPIC

      #end
      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
      #foreach($field in $model.getInputs())
      #set( $className = $field.getType().getName() )
      transportService.removeReceiver(this::receive${className},
                                      ${model.getName()}TransportTopics.TRACK_ENGAGEMENT_STATUS); //TODO address .track_engagement_status

      #end
      #foreach($field in $model.getOutputs())
      #set( $className = $field.getType().getName() )
      eventService.removeSubscriber(this::send${className},
                                    ${className}.TOPIC); //TODO address .TOPIC

      #end

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
   #foreach($field in $model.getInputs())
   #set( $className = $field.getType().getName() )
   #set( $fieldName = $field.getName() )

   private void receive${className}(ITransportObject transportObject,
                                             ${model.getName()}TransportTopics transportTopic) {
      preReceiveMessage(${model.getName()}TransportTopics.TRACK_ENGAGEMENT_STATUS); //TODO handle transport topic type
      try {
         ${className}Wrapper.${className} ${fieldName};
         try {
            ${fieldName} =
               ${className}Wrapper.${className}.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(convert(${fieldName};), ${className}.TOPIC); //TODO handle transport topic type
      } finally {
         postReceiveMessage(${model.getName()}TransportTopics.TRACK_ENGAGEMENT_STATUS); //TODO handle transport topic type
      }
   }
   #end
   #foreach($field in $model.getOutputs())
   #set( $className = $field.getType().getName() )
   #set( $fieldName = $field.getName() )

   private void send${className}(IEvent<${className}> event) {
      preSendMessage(${model.getName()}TransportTopics.TRACK_PRIORITY);
      try {
         ${className} from = event.getSource();
         ${className}Wrapper.${className} to = convert(from);
         transportService.send(ITransportObject.withPayload(to.toByteArray()),
                               ${model.getName()}TransportTopics.TRACK_PRIORITY); //TODO handle transport topic type
         postSendMessage(${model.getName()}TransportTopics.TRACK_PRIORITY); //TODO handle transport topic type
      } finally {
         postSendMessage(${model.getName()}TransportTopics.TRACK_PRIORITY); //TODO handle transport topic type
      }
   }
   #end
   #foreach($field in $model.getInputs())
   #set( $className = $field.getType().getName() )
   #set( $fieldName = $field.getName() )

   private ${className} convert(${className}Wrapper.${className} from) {
      ${className} to = new ${className}();
      #foreach ($subField in $field.getType().getFields())
      #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
      #set( $fieldNameTail =  $subField.getName().substring(1))
      to.set${fieldCapLetter}${fieldNameTail}(from.get${fieldCapLetter}${fieldNameTail}());
      #end

      return to;
   }
   #end

   #foreach($field in $model.getOutputs())
   #set( $className = $field.getType().getName() )
   #set( $fieldName = $field.getName() )

   private ${className}Wrapper.${className} convert(${className} from) {
      ${className} to = new ${className}();
      #foreach ($subField in $field.getType().getFields())
      #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
      #set( $fieldNameTail =  $subField.getName().substring(1))
      to.set${fieldCapLetter}${fieldNameTail}(from.get${fieldCapLetter}${fieldNameTail}());
      #end

      return to;
   }
   #end

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

      switch(transportTopic){
         #foreach($field in $model.getInputs())
         #set( $className = $field.getType().getName() )
         #set( $fieldName = $field.getName() )

         case ${model.getName()}TransportTopics.${fieldName}:
            requirements = Arrays.asList(
            #foreach($req in $modelRequirements)
               ${req},
            #end
            );
            break;
            #end

         #foreach($field in $model.getOutputs())
         #set( $className = $field.getType().getName() )
         #set( $fieldName = $field.getName() )

         case ${model.getName()}TransportTopics.${fieldName}:
            requirements = Arrays.asList(
            #foreach($req in $modelRequirements)
               ${req},
            #end
            );
            break;
            #end

         default:
            //do nothing
            break;
      }

      return requirements;
   }
}