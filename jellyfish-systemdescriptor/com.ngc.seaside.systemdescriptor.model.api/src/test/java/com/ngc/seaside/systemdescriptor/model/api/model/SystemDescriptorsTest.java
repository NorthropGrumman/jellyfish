/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
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
   private IDataReferenceField dataRefField;

   @Mock
   private IModelReferenceField modelField;

   @Mock
   private IScenarioStep step;

   @Mock
   private IDataField dataField;

   @Before
   public void setup() {
      when(parent.getInputs()).thenReturn(dataChildren);
      when(parent.getOutputs()).thenReturn(dataChildren);

      when(parent.getParts()).thenReturn(modelChildren);
      when(parent.getRequiredModels()).thenReturn(modelChildren);

      when(scenario.getGivens()).thenReturn(steps);
      when(scenario.getWhens()).thenReturn(steps);
      when(scenario.getThens()).thenReturn(steps);
   }

   @Test
   public void testDoesDetermineIfFieldIsInput() {
      when(dataRefField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataRefField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be input!",
                 SystemDescriptors.isInput(dataRefField));
      assertFalse("should not be input!",
                  SystemDescriptors.isInput(dataRefField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotInput() {
      when(dataRefField.getParent()).thenReturn(null);
      assertFalse("should not be input!",
                  SystemDescriptors.isInput(dataRefField));
   }

   @Test
   public void testDoesDetermineIfFieldIsOutput() {
      when(dataRefField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataRefField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be output!",
                 SystemDescriptors.isOutput(dataRefField));
      assertFalse("should not be output!",
                  SystemDescriptors.isOutput(dataRefField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotOutput() {
      when(dataRefField.getParent()).thenReturn(null);
      assertFalse("should not be output!",
                  SystemDescriptors.isOutput(dataRefField));
   }

   @Test
   public void testDoesDetermineIfFieldIsPart() {
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
   public void testDoesDetermineIfFieldIsNotPart() {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be part!",
                  SystemDescriptors.isPart(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsRequired() {
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
   public void testDoesDetermineIfFieldIsNotRequired() {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be requirement!",
                  SystemDescriptors.isRequired(modelField));
   }

   @Test
   public void testDoesDetermineIfStepIsGiven() {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be given step!",
                 SystemDescriptors.isGivenStep(step));
      steps.remove(step);
      assertFalse("should not be given step!",
                  SystemDescriptors.isGivenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotGiven() {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be given step!",
                  SystemDescriptors.isGivenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsWhen() {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be when step!",
                 SystemDescriptors.isWhenStep(step));
      steps.remove(step);
      assertFalse("should not be when step!",
                  SystemDescriptors.isWhenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotWhen() {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be when step!",
                  SystemDescriptors.isWhenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsThen() {
      when(step.getParent()).thenReturn(scenario);

      steps.add(step);
      assertTrue("should be then step!",
                 SystemDescriptors.isThenStep(step));
      steps.remove(step);
      assertFalse("should not be then step!",
                  SystemDescriptors.isThenStep(step));
   }

   @Test
   public void testDoesDetermineIfStepIsNotThen() {
      when(step.getParent()).thenReturn(null);
      assertFalse("should not be then step!",
                  SystemDescriptors.isThenStep(step));
   }

   @Test
   public void testDoesDetermineIfDataFieldIsForPrimitiveType() {
      when(dataField.getType())
            .thenReturn(DataTypes.INT)
            .thenReturn(DataTypes.DATA)
            .thenReturn(DataTypes.ENUM);
      assertTrue("field should be a primitive data field!",
                 SystemDescriptors.isPrimitiveDataFieldDeclaration(dataField));
      assertFalse("field should not be a primitive data field!",
                  SystemDescriptors.isPrimitiveDataFieldDeclaration(dataField));
      assertFalse("field should not be a primitive data field!",
                  SystemDescriptors.isPrimitiveDataFieldDeclaration(dataField));
   }

   @Test
   public void testDoesDetermineIfModelsAreRelated() {
      IModel a = mock(IModel.class);
      IModel b = mock(IModel.class);
      IModel c = mock(IModel.class);

      when(a.getRefinedModel()).thenReturn(Optional.empty());
      when(b.getRefinedModel()).thenReturn(Optional.of(a));
      when(c.getRefinedModel()).thenReturn(Optional.empty());

      assertTrue("b refines a so models should be related!",
                 SystemDescriptors.areModelsRelated(a, b));
      assertTrue("b refines a so models should be related!",
                 SystemDescriptors.areModelsRelated(b, a));
      assertFalse("neither a nor c refines any models so they should not be related!",
                  SystemDescriptors.areModelsRelated(a, c));
      assertFalse("neither a nor c refines any models so they should not be related!",
                  SystemDescriptors.areModelsRelated(c, a));
      assertTrue("a model should be related to itself!",
                 SystemDescriptors.areModelsRelated(a, a));
   }

   @SuppressWarnings({"unchecked"})
   @Test
   public void testDoesGetSourceAndTargetFieldsOfLinks() {
      IModelReferenceField sourceField = mock(IModelReferenceField.class);
      IModelReferenceField targetField = mock(IModelReferenceField.class);
      IModelLink<IDataReferenceField> link = mock(IModelLink.class);

      assertFalse("source field of link should not be present!",
                  SystemDescriptors.getReferencedFieldOfLinkSource(link).isPresent());
      assertFalse("target field of link should not be present!",
                  SystemDescriptors.getReferencedFieldOfLinkTarget(link).isPresent());

      doAnswer((Answer<Object>) invocationOnMock -> {
         ((Consumer<IModelReferenceField>) invocationOnMock.getArgument(0)).accept(sourceField);
         return null;
      }).when(link).traverseLinkSourceExpression(any());
      doAnswer((Answer<Object>) invocationOnMock -> {
         ((Consumer<IModelReferenceField>) invocationOnMock.getArgument(0)).accept(targetField);
         return null;
      }).when(link).traverseLinkTargetExpression(any());

      assertEquals("source field not correct",
                   sourceField,
                   SystemDescriptors.getReferencedFieldOfLinkSource(link).get());
      assertEquals("target field not correct!",
                   targetField,
                   SystemDescriptors.getReferencedFieldOfLinkTarget(link).get());
   }

   @SuppressWarnings({"unchecked"})
   @Test
   public void testDoesDetermineIfLinkIsForDataOrModel() {
      IModelReferenceField sourceModelField = mock(IModelReferenceField.class);
      IModelLink<IModelReferenceField> modelLink = mock(IModelLink.class);
      when(modelLink.getSource()).thenReturn(sourceModelField);

      IDataReferenceField sourceDataField = mock(IDataReferenceField.class);
      IModelLink<IDataReferenceField> dataLink = mock(IModelLink.class);
      when(dataLink.getSource()).thenReturn(sourceDataField);

      assertTrue("should be a link to a model!",
                 SystemDescriptors.isLinkToModel(modelLink));
      assertFalse("should not be a link to a model!",
                  SystemDescriptors.isLinkToModel(dataLink));

      assertTrue("should be a link to data!",
                 SystemDescriptors.isLinkToData(dataLink));
      assertFalse("should not be a link to data!",
                  SystemDescriptors.isLinkToData(modelLink));
   }
}
