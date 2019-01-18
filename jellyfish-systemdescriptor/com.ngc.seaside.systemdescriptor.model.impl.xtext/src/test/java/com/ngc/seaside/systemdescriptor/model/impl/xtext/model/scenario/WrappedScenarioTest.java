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

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class WrappedScenarioTest extends AbstractWrappedXtextTest {

   private WrappedScenario wrappedScenario;

   private Scenario scenario;

   @Mock
   private IModel parent;

   @Before
   public void setup() throws Throwable {
      scenario = factory().createScenario();
      scenario.setName("scenario1");
      scenario.setGiven(factory().createGivenDeclaration());
      scenario.setWhen(factory().createWhenDeclaration());
      scenario.setThen(factory().createThenDeclaration());

      GivenStep given = factory().createGivenStep();
      given.setKeyword("givenKeyword");
      given.getParameters().add("givenParam");
      scenario.getGiven().getSteps().add(given);

      WhenStep when = factory().createWhenStep();
      when.setKeyword("whenKeyword");
      when.getParameters().add("whenParam");
      scenario.getWhen().getSteps().add(when);

      ThenStep then = factory().createThenStep();
      then.setKeyword("thenKeyword");
      then.getParameters().add("thenParam");
      scenario.getThen().getSteps().add(then);

      Model model = factory().createModel();
      model.getScenarios().add(scenario);
      when(resolver().getWrapperFor(model)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedScenario = new WrappedScenario(resolver(), scenario);
      assertEquals("name not correct!",
                   wrappedScenario.getName(),
                   scenario.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedScenario.getParent());
      assertNotNull("metadata not set!",
                    wrappedScenario.getMetadata());

      IScenarioStep step = wrappedScenario.getGivens().iterator().next();
      assertEquals("given step keyword not correct!",
                   step.getKeyword(),
                   scenario.getGiven().getSteps().get(0).getKeyword());
      assertEquals("given step parameter not correct!",
                   step.getParameters().get(0),
                   scenario.getGiven().getSteps().get(0).getParameters().get(0));

      step = wrappedScenario.getWhens().iterator().next();
      assertEquals("when step keyword not correct!",
                   step.getKeyword(),
                   scenario.getWhen().getSteps().get(0).getKeyword());
      assertEquals("when step parameter not correct!",
                   step.getParameters().get(0),
                   scenario.getWhen().getSteps().get(0).getParameters().get(0));

      step = wrappedScenario.getThens().iterator().next();
      assertEquals("then step keyword not correct!",
                   step.getKeyword(),
                   scenario.getThen().getSteps().get(0).getKeyword());
      assertEquals("then step parameter not correct!",
                   step.getParameters().get(0),
                   scenario.getThen().getSteps().get(0).getParameters().get(0));
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrappedScenario = new WrappedScenario(resolver(), scenario);
      IScenarioStep given = wrappedScenario.getGivens().iterator().next();

      given.setKeyword("newKeyword");
      assertEquals("did not update keyword!",
                   "newKeyword",
                   scenario.getGiven().getSteps().get(0).getKeyword());

      given.getParameters().add("new param");
      assertEquals("did not update param!",
                   "new param",
                   scenario.getGiven().getSteps().get(0).getParameters().get(1));
   }

   @Test
   public void testDoesWrapEmptyXtextEScenario() throws Throwable {
      scenario.setGiven(null);
      scenario.setWhen(null);
      scenario.setThen(null);

      wrappedScenario = new WrappedScenario(resolver(), scenario);
      assertTrue("steps should be empty!",
                 wrappedScenario.getGivens().isEmpty());
      assertTrue("steps should be empty!",
                 wrappedScenario.getWhens().isEmpty());
      assertTrue("steps should be empty!",
                 wrappedScenario.getThens().isEmpty());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrappedScenario = new WrappedScenario(resolver(), scenario);

      Scenario xtext = WrappedScenario.toXtextScenario(wrappedScenario);
      assertEquals("name not correct!",
                   scenario.getName(),
                   xtext.getName());
      assertEquals("given steps not correct!",
                   scenario.getGiven().getSteps().get(0).getKeyword(),
                   xtext.getGiven().getSteps().get(0).getKeyword());
      assertEquals("when steps not correct!",
                   scenario.getWhen().getSteps().get(0).getKeyword(),
                   xtext.getWhen().getSteps().get(0).getKeyword());
      assertEquals("then steps not correct!",
                   scenario.getThen().getSteps().get(0).getKeyword(),
                   xtext.getThen().getSteps().get(0).getKeyword());
   }
}
