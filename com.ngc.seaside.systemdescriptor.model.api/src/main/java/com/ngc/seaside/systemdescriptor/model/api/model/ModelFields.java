package com.ngc.seaside.systemdescriptor.model.api.model;

/**
 * Contains utilities to help with determining if a field is an input, output, part, or requirement.
 */
public class ModelFields {

   private ModelFields() {
   }

   /**
    * Returns true if the given field is an input of its parent.
    */
   public static boolean isInput(IDataReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getInputs().contains(field);
   }

   /**
    * Returns true if the given field is an output of its parent.
    */
   public static boolean isOutput(IDataReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getOutputs().contains(field);
   }

   /**
    * Returns true if the given field is a part of its parent.
    */
   public static boolean isPart(IModelReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getParts().contains(field);
   }

   /**
    * Returns true if the given field is a required model of its parent.
    */
   public static boolean isRequired(IModelReferenceField field) {
      if (field == null) {
         throw new NullPointerException("field may not be null!");
      }
      return field.getParent() != null
             && field.getParent().getRequiredModels().contains(field);
   }
}
