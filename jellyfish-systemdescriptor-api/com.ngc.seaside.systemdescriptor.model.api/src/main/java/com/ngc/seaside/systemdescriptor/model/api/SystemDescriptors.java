package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

/**
 * Contains various utility methods to assist with working with an {@code ISystemDescriptor}.
 */
public class SystemDescriptors {

   private SystemDescriptors() {
   }

   /**
    * Returns true if the given field is an input of its parent model.
    */
   public static boolean isInput(IDataReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getInputs().contains(field);
   }

   /**
    * Returns true if the given field is an output of its parent model.
    */
   public static boolean isOutput(IDataReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getOutputs().contains(field);
   }

   /**
    * Returns true if the given field is a part of its parent model.
    */
   public static boolean isPart(IModelReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getParts().contains(field);
   }

   /**
    * Returns true if the given field is a required model of its parent model.
    */
   public static boolean isRequired(IModelReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getRequiredModels().contains(field);
   }

   /**
    * Returns true if the step is a given step of its parent scenario.
    */
   public static boolean isGivenStep(IScenarioStep step) {
      if (step == null) {
         throw new NullPointerException("step may not be null!");
      }
      return step.getParent() != null
             && step.getParent().getGivens().contains(step);
   }

   /**
    * Returns true if the step is a when step of its parent scenario.
    */
   public static boolean isWhenStep(IScenarioStep step) {
      if (step == null) {
         throw new NullPointerException("step may not be null!");
      }
      return step.getParent() != null
             && step.getParent().getWhens().contains(step);
   }

   /**
    * Returns true if the step is a then step of its parent scenario.
    */
   public static boolean isThenStep(IScenarioStep step) {
      if (step == null) {
         throw new NullPointerException("step may not be null!");
      }
      return step.getParent() != null
             && step.getParent().getThens().contains(step);
   }

   /**
    * Returns true if the {@code IDataField} references a primitive type.
    */
   public static boolean isPrimitiveDataFieldDeclaration(IDataField field) {
      return field.getType() != DataTypes.DATA && field.getType() != DataTypes.ENUM;
   }
}
