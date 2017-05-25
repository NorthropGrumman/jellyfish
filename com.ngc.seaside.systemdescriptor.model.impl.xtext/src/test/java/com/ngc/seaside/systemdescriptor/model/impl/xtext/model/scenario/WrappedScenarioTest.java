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

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
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

    IScenarioStep step = mock(IScenarioStep.class);
    when(step.getKeyword()).thenReturn("do");
    when(step.getParameters()).thenReturn(Collections.singletonList("something"));
    wrappedScenario.getGivens().add(step);

    assertEquals("did not add new step!",
                 "do",
                 scenario.getGiven().getSteps().get(0).getKeyword());
    assertEquals("did not add new step!",
                 "something",
                 scenario.getGiven().getSteps().get(0).getParameters().get(0));
  }
}
