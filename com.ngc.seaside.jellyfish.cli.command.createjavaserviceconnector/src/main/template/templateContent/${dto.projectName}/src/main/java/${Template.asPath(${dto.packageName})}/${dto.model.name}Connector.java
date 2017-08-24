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
#foreach($input in $dto.model.inputs)
      transportService.addReceiver(this::receive${input.type.name}, 
         ${dto.model.name}TransportTopics.${input.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});

#end
#foreach($output in $dto.model.outputs)
      eventService.addSubscriber(this::send${output.type.name}, 
         ${dto.basePackage}.events.${output.type.name}.TOPIC);

#end
      logService.debug(getClass(), "Activated.");
   }

   @SuppressWarnings("unchecked")
   @Deactivate
   public void deactivate() {
#foreach($input in ${dto.model.inputs})
      transportService.removeReceiver(this::receive${input.type.name},
         ${dto.model.name}TransportTopics.${input.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});

#end
#foreach($output in ${dto.model.outputs})
      eventService.removeSubscriber(this::send${output.type.name},
         ${dto.model.parent.name}.${dto.model.name.toLowerCase()}.events.${output.type.name}.TOPIC);

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
#foreach( $input in ${dto.model.inputs} )

   private void receive${input.type.name}(ITransportObject transportObject,
                                          ${dto.model.name}TransportTopics transportTopic) {
      preReceiveMessage(${dto.model.name}TransportTopics.${input.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});
      try {
         ${input.type.fullyQualifiedName}Wrapper.${input.type.name} ${input.name};
         try {
            ${input.name} =
               ${input.type.fullyQualifiedName}Wrapper.${input.type.name}.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e);
         }
         eventService.publish(convert(${input.name}), ${dto.basePackage}.events.${input.type.name}.TOPIC);
      } finally {
         postReceiveMessage(${dto.model.name}TransportTopics.${input.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});
      }
   }
#end
#foreach( $output in ${dto.model.outputs} )

   private void send${output.type.name}(IEvent<${dto.basePackage}.events.${output.type.name}> event) {
      preSendMessage(${dto.model.name}TransportTopics.${output.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});
      ${dto.basePackage}.events.${output.type.name} from = event.getSource();
      ${output.type.fullyQualifiedName}Wrapper.${output.type.name} to = convert(from);
      try {
         transportService.send(ITransportObject.withPayload(to.toByteArray()), 
            ${dto.model.name}TransportTopics.${output.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});
      } finally {
         postSendMessage(${dto.model.name}TransportTopics.${output.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()});
      }
   }
#end
#foreach($data in ${dto.allInputData})

   private static ${dto.basePackage}.events.${data.name} convert(${data.fullyQualifiedName}Wrapper.${data.name} from) {
      ${dto.basePackage}.events.${data.name} to = new ${dto.basePackage}.events.${data.name}();

#foreach ($field in $data.fields)
#set( $fieldCap = "${field.name.substring(0, 1).toUpperCase()}${field.name.substring(1)}" )
#if ( $field.type == "DATA" || $field.type == "ENUM" )
#if ( $field.type == "DATA")
#set ( $name = $field.referencedDataType.name )
#else
#set ( $name = $field.referencedEnumeration.name )
#end
#if ( $field.cardinality == "MANY" )
      to.set${fieldCap}(new java.util.ArrayList<>(from.get${fieldCap}Count()));
      for (${field.type.fullyQualifiedName}Wrapper.${name} value : from.get${fieldCap}List()) {
         to.get${fieldCap}().add(convert(value));
      }
#else
      to.set${fieldCap}(convert(from.get${fieldCap}()));
#end
#else
#if ( $field.cardinality == "MANY" )
      to.set${fieldCap}(new java.util.ArrayList<>(from.get${fieldCap}List()));
#else
      to.set${fieldCap}(from.get${fieldCap}());
#end
#end
#end

      return to;
   }
#end
#foreach($data in ${dto.allOutputData})

   private static ${data.fullyQualifiedName}Wrapper.${data.name} convert(${dto.basePackage}.events.${data.name} from) {
      ${data.fullyQualifiedName}Wrapper.${data.name}.Builder to = ${data.fullyQualifiedName}Wrapper.${data.name}.newBuilder();

#foreach ( $field in $data.fields )
#set( $fieldCap = "${field.name.substring(0, 1).toUpperCase()}${field.name.substring(1)}" )
#if ( $field.type == "DATA" || $field.type == "ENUM" )
#if ( $field.type == "DATA")
#set ( $name = $field.referencedDataType.name )
#else
#set ( $name = $field.referencedEnumeration.name )
#end
#if ( $field.cardinality == "MANY" )
      for (${dto.basePackage}.events.${name} value : from.get${fieldCap}()) {
         to.add${fieldCap}(convert(value));
      }
#else
      to.set${fieldCap}(convert(from.get${fieldCap}()));
#end
#else
#if ( $field.cardinality == "MANY" )
      to.addAll{fieldCap}(from.get${fieldCap}());
#else
      to.set${fieldCap}(from.get${fieldCap}());
#end
#end
#end      

      return to.build();
   }
#end
#foreach ( $enum in ${dto.allInputEnums} )

   private static ${dto.basePackage}.events.${enum.name} convert(${enum.fullyQualifiedName}Wrapper.${enum.name} from) {
      final ${dto.basePackage}.events.${enum.name} to;
      switch (from) {
#foreach ( $value in $enum.values)
      case $value:
         to = ${dto.basePackage}.events.${enum.name}.$value;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }
      return to;
   }
#end
#foreach ( $enum in ${dto.allInputEnums} )

   private static ${enum.fullyQualifiedName}Wrapper.${enum.name} convert(${dto.basePackage}.events.${enum.name} from) {
      final ${enum.fullyQualifiedName}Wrapper.${enum.name} to;
      switch (from) {
#foreach ( $value in $enum.values)
      case $value:
         to = ${enum.fullyQualifiedName}Wrapper.${enum.name}.$value;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }
      return to;
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
#foreach( $field in $dto.model.inputs )
         case ${field.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()}:
            requirements = Arrays.asList(
#set ($size = $dto.requirements.size())
#foreach($req in $dto.requirements)
#if($velocityCount == $size)
               ${req}
#else
               ${req},
#end
#end
            );
            break;
#end
#foreach($field in $dto.model.outputs)
         case ${field.type.name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase()}:
            requirements = Arrays.asList(
#set ($size = $dto.requirements.size())
#foreach($req in $dto.requirements)
#if($velocityCount == $size)
               ${req}
#else
               ${req},
#end
#end
            );
            break;
#end
         default:
            requirements = Collections.emptyList();
            break;
      }

      return requirements;
   }
}
