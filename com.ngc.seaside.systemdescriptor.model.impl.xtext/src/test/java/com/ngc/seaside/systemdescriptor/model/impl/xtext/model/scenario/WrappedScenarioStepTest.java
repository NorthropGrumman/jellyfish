package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedScenarioStepTest extends AbstractWrappedXtextTest {

  private WrappedScenarioStep<GivenStep> wrapped;

  private GivenStep step;

  @Before
  public void setup() throws Throwable {
    step = factory().createGivenStep();
    step.setKeyword("test");
    step.getParameters().add("param1");
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
