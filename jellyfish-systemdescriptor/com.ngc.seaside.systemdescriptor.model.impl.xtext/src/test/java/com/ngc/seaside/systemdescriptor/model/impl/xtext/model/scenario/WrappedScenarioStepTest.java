/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedScenarioStepTest extends AbstractWrappedXtextTest {

   private WrappedScenarioStep<GivenStep> wrapped;

   private GivenStep step;

   @Mock
   private IScenario parentScenario;

   @Mock
   private IModel parentModel;

   @Before
   public void setup() throws Throwable {
      step = factory().createGivenStep();
      step.setKeyword("test");
      step.getParameters().add("param1");

      Scenario scenario = factory().createScenario();
      scenario.setName("scenarioOne");
      scenario.setGiven(factory().createGivenDeclaration());
      scenario.getGiven().getSteps().add(step);

      Model model = factory().createModel();
      model.getScenarios().add(scenario);

      when(parentScenario.getName()).thenReturn(scenario.getName());

      INamedChildCollection<IModel, IScenario> scenarioCollection = new NamedChildCollection<>();
      scenarioCollection.add(parentScenario);

      when(resolver().getWrapperFor(model)).thenReturn(parentModel);
      when(parentModel.getScenarios()).thenReturn(scenarioCollection);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedScenarioStep<>(resolver(), step);
      assertEquals("keyword not correct!",
                   step.getKeyword(),
                   wrapped.getKeyword());
      assertEquals("parameters not correct!",
                   step.getParameters(),
                   wrapped.getParameters());
      assertEquals("parent not correct!",
                   parentScenario,
                   wrapped.getParent());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrapped = new WrappedScenarioStep<>(resolver(), step);
      wrapped.setKeyword("newKeyword");
      assertEquals("keyword not correct!",
                   "newKeyword",
                   step.getKeyword());

      wrapped.getParameters().add("newParam");
      assertEquals("parameters not correct!",
                   "newParam",
                   step.getParameters().get(1));
   }

   @Test
   public void testDoesConvertToXtext() throws Throwable {
      IScenarioStep step = mock(IScenarioStep.class);
      when(step.getKeyword()).thenReturn("keyword");
      when(step.getParameters()).thenReturn(Collections.singletonList("param1"));

      GivenStep given = WrappedScenarioStep.toXtextGivenStep(step);
      assertEquals("keyword not correct!",
                   step.getKeyword(),
                   given.getKeyword());
      assertEquals("parameters not correct!",
                   step.getParameters().get(0),
                   given.getParameters().get(0));

      WhenStep when = WrappedScenarioStep.toXtextWhenStep(step);
      assertEquals("keyword not correct!",
                   step.getKeyword(),
                   when.getKeyword());
      assertEquals("parameters not correct!",
                   step.getParameters().get(0),
                   when.getParameters().get(0));

      ThenStep then = WrappedScenarioStep.toXtextThenStep(step);
      assertEquals("keyword not correct!",
                   step.getKeyword(),
                   then.getKeyword());
      assertEquals("parameters not correct!",
                   step.getParameters().get(0),
                   then.getParameters().get(0));
   }
}
