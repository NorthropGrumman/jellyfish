package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

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
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TemplateDtoFactoryTest {

   private TemplateDtoFactory factory;

   private Model model;

   private String packagez = "com.ngc.seaside.threateval.engagementtrackpriorityservice";

   @Before
   public void setup() throws Throwable {
      model = newModelForTesting();
      factory = new TemplateDtoFactory();
   }

   @Test
   public void testDoesCreateDao() throws Throwable {
      TemplateDto dto = factory.newDto(model, packagez);

      assertEquals("className not correct!",
                   "EngagementTrackPriorityService",
                   dto.getClassName());
      assertEquals("packageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.impl",
                   dto.getPackageName());
      assertEquals("artifactId not correct!",
                   "engagementtrackpriorityservice",
                   dto.getArtifactId());

      assertNotNull("base class dto not set!",
                    dto.getAbstractServiceDto());
      assertEquals("baseClassName not correct!",
                   "AbstractEngagementTrackPriorityService",
                   dto.getAbstractServiceDto().getClassName());
      assertEquals("baseClassPackageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
                   dto.getAbstractServiceDto().getPackageName());

      assertNotNull("interface dto not set!",
                    dto.getServiceInterfaceDto());
      assertEquals("interfaceName not correct!",
                   "IEngagementTrackPriorityService",
                   dto.getServiceInterfaceDto().getInterfaceName());
      assertEquals("interfacePackageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dto.getServiceInterfaceDto().getPackageName());

      assertEquals("wrong number of methods!",
                   1,
                   dto.getMethods().size());

      MethodDto m = dto.getMethods().get(0);
      assertEquals("methodName not correct!",
                   "calculateTrackPriority",
                   m.getMethodName());
      assertTrue("method should return a value!",
                 m.isReturns());
      assertTrue("method should override!",
                 m.isOverride());
      assertEquals("method return type not correct!",
                   "TrackPriority",
                   m.getReturnArgument().getArgumentClassName());
      assertEquals("method return type package not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.events",
                   m.getReturnArgument().getArgumentPackageName());

      assertEquals("arguments count is incorrect for method!",
                   1,
                   m.getArguments().size());
      ArgumentDto arg = m.getArguments().get(0);
      assertEquals("arg type not correct!",
                   "TrackEngagementStatus",
                   arg.getArgumentClassName());
      assertEquals("arg type package not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.events",
                   arg.getArgumentPackageName());

      assertTrue("missing import", dto.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.api.IEngagementTrackPriorityService"));
      assertTrue("missing import", dto.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl.AbstractEngagementTrackPriorityService"));
      assertTrue("missing import", dto.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus"));
      assertTrue("missing import", dto.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority"));
   }

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
