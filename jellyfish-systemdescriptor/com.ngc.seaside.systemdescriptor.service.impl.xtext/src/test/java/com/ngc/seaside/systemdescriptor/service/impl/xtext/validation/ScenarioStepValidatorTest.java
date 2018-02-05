package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ScenarioStepValidatorTest {

   private ScenarioStepValidator validator;

   @Mock
   private ISystemDescriptorService service;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Mock
   private IScenarioStep step;

   @Mock
   private IScenario scenario;

   @Mock
   private IScenarioStepHandler handler;

   @Before
   public void setup() throws Throwable {
      when(step.getParent()).thenReturn(scenario);
      when(context.getObject()).thenReturn(step);
      when(context.declare(any(), any(), any())).thenReturn(step);

      validator = new ScenarioStepValidator(service);
   }

   @Test
   public void testDoesValidateGivenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("foo");
      when(scenario.getGivens()).thenReturn(Collections.singletonList(step));
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.PAST_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context, never()).declare(any(), any(), eq(step));
   }

   @Test
   public void testDoesValidateMissingGivenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("bar");
      when(scenario.getGivens()).thenReturn(Collections.singletonList(step));
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.PAST_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context).declare(eq(Severity.ERROR), anyString(), eq(step));
   }

   @Test
   public void testDoesValidateWhenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("foo");
      when(scenario.getWhens()).thenReturn(Collections.singletonList(step));
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.PRESENT_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context, never()).declare(any(), any(), eq(step));
   }

   @Test
   public void testDoesValidateMissingWhenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("bar");
      when(scenario.getWhens()).thenReturn(Collections.singletonList(step));
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.PRESENT_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context).declare(eq(Severity.ERROR), anyString(), eq(step));
   }

   @Test
   public void testDoesValidateThenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("foo");
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.FUTURE_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context, never()).declare(any(), any(), eq(step));
   }

   @Test
   public void testDoesValidateMissingThenStep() throws Throwable {
      when(step.getKeyword()).thenReturn("bar");
      when(handler.getVerbs()).thenReturn(mapOf("foo", VerbTense.FUTURE_TENSE));
      when(service.getScenarioStepHandlers()).thenReturn(Collections.singletonList(handler));

      validator.validateStep(context);

      verify(context).declare(eq(Severity.ERROR), anyString(), eq(step));
   }

   private static Map<VerbTense, ScenarioStepVerb> mapOf(String verb, VerbTense tense) {
      Map<VerbTense, ScenarioStepVerb> map = new EnumMap<>(VerbTense.class);
      map.put(tense, ScenarioStepVerb.create(verb, tense));
      return map;
   }
}
