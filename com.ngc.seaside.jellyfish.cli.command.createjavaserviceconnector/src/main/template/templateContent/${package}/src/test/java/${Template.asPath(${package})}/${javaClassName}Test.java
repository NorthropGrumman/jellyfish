package ${package};

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.IEventSubscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.event.MockedEvents;
import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportReceiver;
import com.ngc.seaside.service.transport.api.ITransportService;
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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ${javaClassName}Test {

   private static final int ENGAGEMENT_COUNT = 10;
   private static final int TRACK_ID = 20;
   private static final float KILL_PROBABILITY = 0.75f;
   private static final float PRIORITY = 0.25f;
   private static final String SOURCE_ID = "mock_source";

   private ${javaClassName} connector;

   @Mock
   private ITransportService transportService;

   @Mock
   private IEventService eventService;

   @Mock
   private ILogService logService = Mockito.mock(ILogService.class);

   #foreach($field in $model.getInputs())
   #set( $className = $field.getType().getName() )
   private final ${className}Wrapper.${className} ${field.getName()} =
      ${className}Wrapper.${className}.newBuilder()
      #foreach ($subField in $field.getType().getFields())
      #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
      #set( $fieldNameTail =  $subField.getName().substring(1))
      .set${fieldCapLetter}${fieldNameTail}(${subField.getName().toUpperCase()});
      #end
      .build();
   #end

   #foreach($field in $model.getOutputs())
   #set( $className = $field.getType().getName() )
   private final ${className} ${field.getName()} = new ${className}()
   #foreach ($subField in $field.getType().getFields())
   #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
   #set( $fieldNameTail =  $subField.getName().substring(1))
      .set${fieldCapLetter}${fieldNameTail}(${subField.getName().toUpperCase()});
   #end
   #end

   @Before
   public void before() throws Throwable {
      connector = new ${javaClassName}();
      connector.setTransportService(transportService);
      connector.setEventService(eventService);
      connector.setLogService(logService);
      connector.activate();
   }

   @Test
   public void doesPublishToEventServiceWhenTransportObjectRecieved() throws Throwable {
      // Verify the connector subscribed to the transport topic and capture the receiver it registered.
      #foreach($field in $model.getInputs())
      #set( $fieldName = $field.getName() )
      #set( $className = $field.getType().getName() )
      ArgumentCaptor<ITransportReceiver<${className}TransportTopics>> captor =
      ArgumentCaptor.forClass(ITransportReceiver.class);
      verify(transportService).addReceiver(captor.capture(),
      eq(${javaClassName}TransportTopics.TRACK_ENGAGEMENT_STATUS)); //TODO Handle topic name

      ITransportObject object = ITransportObject.withPayload(${fieldName}.toByteArray());
      captor.getValue().receive(object, ${javaClassName}TransportTopics.TRACK_ENGAGEMENT_STATUS); //TODO Handle topic name

      // Verify the correct event was published.
      verify(eventService).publish(new ${className}()
         #foreach ($subField in $field.getType().getFields())
         #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
         #set( $fieldNameTail =  $subField.getName().substring(1))
         .set${fieldCapLetter}${fieldNameTail}(${fieldName}.get${fieldCapLetter}${fieldNameTail}())
         #end),
         ${className}.TOPIC); //TODO Handle topic name
      #end

   }

   @Test
   public void doesPublishToTransportServiceWhenEventReceived() throws Throwable {
      // Verify the connector subscribed to the event topic and capture the listener it registered.
      #foreach($field in $model.getOutputs())
      #set( $fieldName = $field.getName() )
      #set( $className = $field.getType().getName() )
      ArgumentCaptor<IEventSubscriber<TrackPriority>> captor =
      ArgumentCaptor.forClass(IEventSubscriber.class);
      verify(eventService).addSubscriber(captor.capture(),
      eq(${className}.TOPIC)); //TODO Handle topic name

      captor.getValue().eventReceived(MockedEvents.of(${fieldName}, ${className}.TOPIC));//TODO Handle topic name

      // Verify the correct transport was published.
      ${className}Wrapper.${className} expected = ${className}Wrapper.${className}.newBuilder()
      #foreach ($subField in $field.getType().getFields())
      #set( $fieldCapLetter = $subField.getName().substring(0, 1).toUpperCase())
      #set( $fieldNameTail =  $subField.getName().substring(1))
      .set${fieldCapLetter}${fieldNameTail}(${fieldName}.get${fieldCapLetter}${fieldNameTail}())
      #end
      .build();
      #end

      verify(transportService).send(eq(ITransportObject.withPayload(expected.toByteArray())),
                                    eq(${javaClassName}TransportTopics.TRACK_PRIORITY));//TODO Handle topic name
   }

   @Test
   public void doesUnsubscribeWhenDeactivated() throws Throwable {
      connector.deactivate();
      #foreach($field in $model.getInputs())
      #set( $className = $field.getType().getName() )
      verify(transportService).removeReceiver(any(ITransportReceiver.class),
                                              eq(${javaClassName}TransportTopics.TRACK_ENGAGEMENT_STATUS));//TODO address .track_engagement_status
      #end
      #foreach($field in $model.getOutputs())
      #set( $className = $field.getType().getName() )
      verify(eventService).removeSubscriber(any(IEventSubscriber.class),
                                            eq(${className}.TOPIC));//TODO Handle topic name
      #end
   }

   @After
   public void after() throws Throwable {
      connector.deactivate();
   }
}
