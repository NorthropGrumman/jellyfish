package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SystemDescriptorsTest {

   @Mock
   private IModel parent;

   @Mock
   private IScenario scenario;

   private Collection<IScenarioStep> steps = new ArrayList<>();

   @Mock
   private INamedChildCollection<IModel, IDataReferenceField> dataChildren;

   @Mock
   private INamedChildCollection<IModel, IModelReferenceField> modelChildren;

   @Mock
   private IDataReferenceField dataField;

   @Mock
   private IModelReferenceField modelField;

   @Mock
   private IScenarioStep step;

   @Before
   public void setup() throws Throwable {
      when(parent.getInputs()).thenReturn(dataChildren);
      when(parent.getOutputs()).thenReturn(dataChildren);

      when(parent.getParts()).thenReturn(modelChildren);
      when(parent.getRequiredModels()).thenReturn(modelChildren);

      when(scenario.getGivens()).thenReturn(steps);
      when(scenario.getWhens()).thenReturn(steps);
      when(scenario.getThens()).thenReturn(steps);
   }

   @Test
   public void testDoesDetermineIfFieldIsInput() throws Throwable {
      when(dataField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be input!",
                 SystemDescriptors.isInput(dataField));
      assertFalse("should not be input!",
                  SystemDescriptors.isInput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotInput() throws Throwable {
      when(dataField.getParent()).thenReturn(null);
      assertFalse("should not be input!",
                  SystemDescriptors.isInput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsOutput() throws Throwable {
      when(dataField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be output!",
                 SystemDescriptors.isOutput(dataField));
      assertFalse("should not be output!",
                  SystemDescriptors.isOutput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotOutput() throws Throwable {
      when(dataField.getParent()).thenReturn(null);
      assertFalse("should not be output!",
                  SystemDescriptors.isOutput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsPart() throws Throwable {
      when(modelField.getParent()).thenReturn(parent);
      when(modelChildren.contains(modelField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be part!",
                 SystemDescriptors.isPart(modelField));
      assertFalse("should not be part!",
                  SystemDescriptors.isPart(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotPart() throws Throwable {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be part!",
                  SystemDescriptors.isPart(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsRequired() throws Throwable {
      when(modelField.getParent()).thenReturn(parent);
      when(modelChildren.contains(modelField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be requirement!",
                 SystemDescriptors.isRequired(modelField));
      assertFalse("should not be requirement!",
                  SystemDescriptors.isRequired(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotRequired() throws Throwable {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be requirement!",
                  SystemDescriptors.isRequired(modelField));
   }

   @Test
   public void testDoesDetermineIfStepIsGiven() throws Throwable {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be given step!",
                 SystemDescriptors.isGivenStep(step));
      steps.remove(step);
      assertFalse("should not be given step!",
                  SystemDescriptors.isGivenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotGiven() throws Throwable {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be given step!",
                  SystemDescriptors.isGivenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsWhen() throws Throwable {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be when step!",
                 SystemDescriptors.isWhenStep(step));
      steps.remove(step);
      assertFalse("should not be when step!",
                  SystemDescriptors.isWhenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotWhen() throws Throwable {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be when step!",
                  SystemDescriptors.isWhenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsThen() throws Throwable {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be then step!",
                 SystemDescriptors.isThenStep(step));
      steps.remove(step);
      assertFalse("should not be then step!",
                  SystemDescriptors.isThenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotThen() throws Throwable {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be then step!",
                  SystemDescriptors.isThenStep(step));
   }
}
