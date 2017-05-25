package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
