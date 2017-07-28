package ${package}.transport.topic;

import com.ngc.seaside.service.transport.api.ITransportTopic;

/**
 * The transport topics used by the ${modelObject.getName().replaceAll("((^[a-z]+)|([A-Z]{1}[a-z]+)|([A-Z]+(?=([A-Z][a-z])|($))))", "$1 ").toLowerCase()}.
 */
public enum ${modelObject.getName()}TransportTopics implements ITransportTopic {
#set ( $modelOutputSize =  $modelObject.getOutputs().size() - 1)
#if ( $modelOutputSize > 0)
   #set ( $separatorVal = ',')
#else
   #set ( $separatorVal = '')
#end
#foreach($field in $modelObject.getInputs())
   $field.getType().getName().replaceAll("([^_A-Z])([A-Z])", "$1_$2").toUpperCase()$separatorVal
#end
#foreach($field in $modelObject.getOutputs())
   $field.getType().getName().replaceAll("([^_A-Z])([A-Z])", "$1_$2").toUpperCase()#if( $velocityHasNext ),#end

#end
}
