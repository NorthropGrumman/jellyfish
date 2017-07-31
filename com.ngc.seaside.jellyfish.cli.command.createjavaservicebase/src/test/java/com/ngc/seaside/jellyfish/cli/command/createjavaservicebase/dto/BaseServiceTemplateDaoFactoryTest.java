package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class BaseServiceTemplateDaoFactoryTest {

//   @Before
//   public void setup() throws Throwable {
//   }

   public static Model newModelForTesting() {
      Data trackEngagementStatus = new Data("TrackEngagementStatus");
      Data trackPriority = new Data("TrackPriority");

      Scenario calculateTrackPriority = new Scenario("calculateTrackPriority");

      ScenarioStep step = new ScenarioStep();
      step.setKeyword(ReceiveStepHandler.PRESENT.getVerb());
      step.getParameters().add("trackEngagementStatus");
      calculateTrackPriority.setWhens(listOf(step));

      step = new ScenarioStep();
      step.setKeyword(PublishStepHandler.FUTURE.getVerb());
      step.getParameters().add("trackPriority");
      calculateTrackPriority.setThens(listOf(step));

      Model model = new Model("EngagementTrackPriorityService");
      model.addInput(new DataReferenceField("trackEngagementStatus").setType(trackEngagementStatus));
      model.addOutput(new DataReferenceField("trackPriority").setType(trackPriority));
      model.addScenario(calculateTrackPriority);
      calculateTrackPriority.setParent(model);

      Package p = new Package("com.ngc.seaside.threateval");
      p.addModel(model);

      return model;
   }

   private static <T> ArrayList<T> listOf(T... things) {
      // TODO TH:
      // Fix the basic model impl.  ArrayList SHOULD NOT be in the signature.
      return new ArrayList<>(Arrays.asList(things));
   }
}
