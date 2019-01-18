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
package com.ngc.seaside.systemdescriptor.model.api;

import com.google.common.collect.Sets;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

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

   /**
    * Traverse the refinement hierarchy of a model, invoking the given consumer on each model.
    *
    * @param model    the model whose hierarchy is being traversed
    * @param consumer the consumer to invoke for each model
    */
   public static void traverseRefinementHierarchy(IModel model, Consumer<IModel> consumer) {
      if (model == null) {
         throw new NullPointerException("model may not be null!");
      }
      if (consumer == null) {
         throw new NullPointerException("consumer may not be null!");
      }
      do {
         consumer.accept(model);
         model = model.getRefinedModel().orElse(null);
      } while (model != null);
   }

   /**
    * Traverse the extension hierarchy of a data type, invoking the given consumer on each data.
    *
    * @param data     the data whose hierarchy is being traversed
    * @param consumer the consumer to invoke for each data
    */
   public static void traverseDataExtensionHierarchy(IData data, Consumer<IData> consumer) {
      if (data == null) {
         throw new NullPointerException("data may not be null!");
      }
      if (consumer == null) {
         throw new NullPointerException("consumer may not be null!");
      }
      do {
         consumer.accept(data);
         data = data.getExtendedDataType().orElse(null);
      } while (data != null);
   }

   /**
    * Returns true if either {@code a} eventually refines {@code b} or if {@code b} eventually refines {@code a}.  This
    * operation is similar to an "instance of" check for types.
    *
    * @param a the first model
    * @param b the second model
    * @return true if either {@code a} or {@code b} refine one another
    */
   public static boolean areModelsRelated(IModel a, IModel b) {
      if (a == null) {
         throw new NullPointerException("a may not be null!");
      }
      if (b == null) {
         throw new NullPointerException("b may not be null!");
      }
      Set<IModel> hierarchyOfA = new HashSet<>();
      Set<IModel> hierarchyOfB = new HashSet<>();
      traverseRefinementHierarchy(a, hierarchyOfA::add);
      traverseRefinementHierarchy(b, hierarchyOfB::add);
      return !Sets.intersection(hierarchyOfA, hierarchyOfB).isEmpty();
   }

   /**
    * Gets the part or requirement field that is referenced in the source of the link.  If the link's source does not
    * reference a part or requirement field of the model that contains the link, the optional is empty.  Use {@link
    * #isPart(IModelReferenceField)} and {@link #isRequired(IModelReferenceField)} to determine if the returned field is
    * a part or requirement.
    *
    * @param link the link whose referenced source field should be returned
    * @return the field that is referenced in the source of the link or an empty optional if the link's source does not
    * reference a field
    */
   public static Optional<IModelReferenceField> getReferencedFieldOfLinkSource(IModelLink<?> link) {
      if (link == null) {
         throw new NullPointerException("link may not be null!");
      }
      RecordingLinkConsumer consumer = new RecordingLinkConsumer();
      link.traverseLinkSourceExpression(consumer);
      return Optional.ofNullable(consumer.field);
   }

   /**
    * Gets the part or requirement field that is referenced in the target of the link.  If the link's target does not
    * reference a part or requirement field of the model that contains the link, the optional is empty.  Use {@link
    * #isPart(IModelReferenceField)} and {@link #isRequired(IModelReferenceField)} to determine if the returned field is
    * a part or requirement.
    *
    * @param link the link whose referenced target field should be returned
    * @return the field that is referenced in the target of the link or an empty optional if the link's target does not
    * reference a field
    */
   public static Optional<IModelReferenceField> getReferencedFieldOfLinkTarget(IModelLink<?> link) {
      if (link == null) {
         throw new NullPointerException("link may not be null!");
      }
      RecordingLinkConsumer consumer = new RecordingLinkConsumer();
      link.traverseLinkTargetExpression(consumer);
      return Optional.ofNullable(consumer.field);
   }

   /**
    * Returns true if the given link links {@link IModelReferenceField}s together.
    *
    * @param link the link to examine
    * @return true if the link can be safely cast to an instance of {@code IModelLink<IModelReferenceField>}
    */
   public static boolean isLinkToModel(IModelLink<?> link) {
      if (link == null) {
         throw new NullPointerException("link may not be null!");
      }
      // The type of the link's source must match the type of the link's target so we don't need to check both fields.
      return link.getSource() instanceof IModelReferenceField;
   }

   /**
    * Returns true if the given link links {@link IDataReferenceField}s together.
    *
    * @param link the link to examine
    * @return true if the link can be safely cast to an instance of {@code IModelLink<IDataReferenceField>}
    */
   public static boolean isLinkToData(IModelLink<?> link) {
      return !isLinkToModel(link);
   }

   /**
    * Records the field field the consumer is invoked with.
    */
   private static class RecordingLinkConsumer implements Consumer<IModelReferenceField> {

      /**
       * The first field the consumer was invoked with or {@code null} if the consumer was not invoked.
       */
      IModelReferenceField field;

      @Override
      public void accept(IModelReferenceField field) {
         this.field = this.field == null ? field : this.field;
      }
   }
}
