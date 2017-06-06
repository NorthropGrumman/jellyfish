package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModelFieldsTest {

   @Mock
   private IModel parent;

   @Mock
   private INamedChildCollection<IModel, IDataReferenceField> dataChildren;

   @Mock
   private INamedChildCollection<IModel, IModelReferenceField> modelChildren;

   @Mock
   private IDataReferenceField dataField;

   @Mock
   private IModelReferenceField modelField;

   @Before
   public void setup() throws Throwable {
      when(parent.getInputs()).thenReturn(dataChildren);
      when(parent.getOutputs()).thenReturn(dataChildren);

      when(parent.getParts()).thenReturn(modelChildren);
      when(parent.getRequiredModels()).thenReturn(modelChildren);
   }

   @Test
   public void testDoesDetermineIfFieldIsInput() throws Throwable {
      when(dataField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be input!",
                 ModelFields.isInput(dataField));
      assertFalse("should not be input!",
                  ModelFields.isInput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotInput() throws Throwable {
      when(dataField.getParent()).thenReturn(null);
      assertFalse("should not be input!",
                  ModelFields.isInput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsOutput() throws Throwable {
      when(dataField.getParent()).thenReturn(parent);
      when(dataChildren.contains(dataField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be output!",
                 ModelFields.isOutput(dataField));
      assertFalse("should not be output!",
                  ModelFields.isOutput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotOutput() throws Throwable {
      when(dataField.getParent()).thenReturn(null);
      assertFalse("should not be output!",
                  ModelFields.isOutput(dataField));
   }

   @Test
   public void testDoesDetermineIfFieldIsPart() throws Throwable {
      when(modelField.getParent()).thenReturn(parent);
      when(modelChildren.contains(modelField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be part!",
                 ModelFields.isPart(modelField));
      assertFalse("should not be part!",
                  ModelFields.isPart(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotPart() throws Throwable {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be part!",
                  ModelFields.isPart(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsRequired() throws Throwable {
      when(modelField.getParent()).thenReturn(parent);
      when(modelChildren.contains(modelField))
            .thenReturn(true)
            .thenReturn(false);

      assertTrue("should be requirement!",
                 ModelFields.isRequired(modelField));
      assertFalse("should not be requirement!",
                  ModelFields.isRequired(modelField));
   }

   @Test
   public void testDoesDetermineIfFieldIsNotRequired() throws Throwable {
      when(modelField.getParent()).thenReturn(null);
      assertFalse("should not be requirement!",
                  ModelFields.isRequired(modelField));
   }
}
