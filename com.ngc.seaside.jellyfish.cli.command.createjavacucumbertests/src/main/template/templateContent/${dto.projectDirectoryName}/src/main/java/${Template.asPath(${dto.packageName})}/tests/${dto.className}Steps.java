package ${dto.packageName};

import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;

import com.ngc.seaside.service.transport.api.ITransportObject;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.seaside.service.transport.impl.testutils.receiver.BlockingReceiver;
import com.ngc.seaside.threateval.datatype.TrackPriorityWrapper.TrackPriority;
import com.ngc.seaside.threateval.engagementplanning.datatype.TrackEngagementStatusWrapper.TrackEngagementStatus;
import com.ngc.seaside.threateval.engagementtrackpriorityservice.transport.topic.EngagementTrackPriorityServiceTransportTopics;

import org.junit.Assert;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ${dto.className}Steps {

   private TrackEngagementStatus.Builder trackEngagementStatusObj;
   private volatile TrackPriority trackPriorityObj;
   private float absoluteTol;

   private BlockingReceiver<TrackPriority, ${dto.className}TransportTopics> receiver;

   private ITransportService transportService;

   @Before
   public void setup() {
      receiver = new BlockingReceiver<>(transportObject -> {
         try {
            return TrackPriority.parseFrom(transportObject.getPayload());
         } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e.getMessage(), e);
         }
      });

      transportService.addReceiver(receiver, ${dto.className}TransportTopics.TRACK_PRIORITY);
   }

#foreach($method in $dto.methods)
      @Test
      public void ${method.methodName}Test(){
            // TODO Auto-generated method stub
            fail("not implemented");
            }

#end

   @After
   public void cleanup() {
         transportService.removeReceiver(receiver, ${dto.className}TransportTopics.TRACK_PRIORITY);
         }
}
