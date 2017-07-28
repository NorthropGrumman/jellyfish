package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

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
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TemplateDaoFactoryTest {

   private TemplateDaoFactory factory;

   private Model model;

   private String packagez = "com.ngc.seaside.threateval.engagementtrackpriorityservice";

   @Before
   public void setup() throws Throwable {
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

      model = new Model("EngagementTrackPriorityService");
      model.addInput(new DataReferenceField("trackEngagementStatus").setType(trackEngagementStatus));
      model.addOutput(new DataReferenceField("trackPriority").setType(trackPriority));
      model.addScenario(calculateTrackPriority);
      calculateTrackPriority.setParent(model);

      factory = new TemplateDaoFactory();
   }

   @Test
   public void testDoesCreateDao() throws Throwable {
      TemplateDao dao = factory.newDao(model, packagez);

      assertEquals("className not correct!",
                   "EngagementTrackPriorityService",
                   dao.getClassName());
      assertEquals("packageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.impl",
                   dao.getPackageName());

      assertEquals("baseClassName not correct!",
                   "AbstractEngagementTrackPriorityService",
                   dao.getBaseClassName());
      assertEquals("baseClassPackageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
                   dao.getBaseClassPackageName());

      assertEquals("interfaceName not correct!",
                   "IEngagementTrackPriorityService",
                   dao.getInterfaceName());
      assertEquals("interfacePackageName not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dao.getInterfacePackageName());

      assertEquals("wrong number of methods!",
                   1,
                   dao.getMethods().size());

      MethodDao m = dao.getMethods().get(0);
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
      ArgumentDao arg = m.getArguments().get(0);
      assertEquals("arg type not correct!",
                   "TrackEngagementStatus",
                   arg.getArgumentClassName());
      assertEquals("arg type package not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.events",
                   arg.getArgumentPackageName());

      assertTrue("missing import", dao.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.api.IEngagementTrackPriorityService"));
      assertTrue("missing import", dao.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl.AbstractEngagementTrackPriorityService"));
      assertTrue("missing import", dao.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus"));
      assertTrue("missing import", dao.getImports().contains(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority"));
   }

   private static <T> ArrayList<T> listOf(T... things) {
      // TODO TH:
      // Fix the basic model impl.  ArrayList SHOULD NOT be in the signature.
      return new ArrayList<>(Arrays.asList(things));
   }
}
