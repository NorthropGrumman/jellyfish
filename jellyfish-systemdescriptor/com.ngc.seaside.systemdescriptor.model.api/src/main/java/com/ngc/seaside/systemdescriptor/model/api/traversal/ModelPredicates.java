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
package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Contains various model predicates that are often used with {@link Traversals#collectModels(ISystemDescriptor,
 * Predicate)}.
 */
public class ModelPredicates {

   /**
    * The JSON key refereed in the metadata of a model to declare stereotypes.  The value is either a string or an array
    * of strings.
    */
   public static final String STEREOTYPE_MEMBER_NAME = "stereotypes";

   /**
    * Gets a predicate that only accepts a model that contains all the given stereotypes.
    *
    * @param stereotype  the stereotype that the model must contain
    * @param stereotypes the optional stereotypes the model must contain
    * @return a predicate that only accepts a model that contains all the given stereotypes
    */
   public static Predicate<IModel> withAllStereotypes(String stereotype, String... stereotypes) {
      if (stereotype == null) {
         throw new NullPointerException("stereotype may not be null!");
      }
      Collection<String> collection = new ArrayList<>();
      collection.add(stereotype);
      if (stereotypes != null) {
         collection.addAll(Arrays.asList(stereotypes));
      }
      return withAllStereotypes(collection);
   }

   /**
    * Gets a predicate that only accepts a model that contains all the given stereotypes.
    *
    * @param stereotypes the stereotypes the model must contain
    * @return a predicate that only accepts a model that contains all the given stereotypes.
    */
   public static Predicate<IModel> withAllStereotypes(Collection<String> stereotypes) {
      if (stereotypes == null) {
         throw new NullPointerException("stereotypes may not be null!");
      }
      return m -> containsAllStereotypes(m, stereotypes);
   }

   /**
    * Gets a predicate that only accepts a model that contains any of the given stereotypes.
    *
    * @param stereotype  the stereotype that the model must contain
    * @param stereotypes the optional stereotypes the model may contain
    * @return a predicate that only accepts a model that contains any of the given stereotypes
    */
   public static Predicate<IModel> withAnyStereotype(String stereotype, String... stereotypes) {
      if (stereotype == null) {
         throw new NullPointerException("stereotype may not be null!");
      }
      Collection<String> collection = new ArrayList<>();
      collection.add(stereotype);
      if (stereotypes != null) {
         collection.addAll(Arrays.asList(stereotypes));
      }
      return withAnyStereotype(collection);
   }

   /**
    * Gets a predicate that only accepts a model that contains any of the given stereotypes.
    *
    * @param stereotypes the stereotypes the model may contain
    * @return a predicate that only accepts a model that contains any of the given stereotypes
    */
   public static Predicate<IModel> withAnyStereotype(Collection<String> stereotypes) {
      if (stereotypes == null) {
         throw new NullPointerException("stereotypes may not be null!");
      }
      return m -> containsAnyStereotype(m, stereotypes);
   }

   private static boolean containsAnyStereotype(IModel model, Collection<String> stereotypes) {
      IMetadata metadata = model.getMetadata();
      boolean accept = metadata != null;
      if (accept) {
         JsonValue value = metadata.getJson().getOrDefault(STEREOTYPE_MEMBER_NAME, JsonValue.NULL);

         // Tolerate either a single value or an array of values.
         switch (value.getValueType()) {
            case ARRAY:
               accept = false;
               JsonArray array = (JsonArray) value;
               for (int i = 0; i < array.size() && !accept; i++) {
                  accept = stereotypes.contains(array.getString(i));
               }
               break;
            case STRING:
               accept = stereotypes.contains(((JsonString) value).getString());
               break;
            default:
               accept = false;
               break;
         }
      }
      return accept;
   }

   private static boolean containsAllStereotypes(IModel model, Collection<String> stereotypes) {
      IMetadata metadata = model.getMetadata();
      boolean accept = metadata != null;
      if (accept) {
         JsonValue value = metadata.getJson().getOrDefault(STEREOTYPE_MEMBER_NAME, JsonValue.NULL);

         // Tolerate either a single value or an array of values.
         switch (value.getValueType()) {
            case ARRAY:
               Collection<String> missingStereotypes = new ArrayList<>(stereotypes);
               JsonArray array = (JsonArray) value;
               for (int i = 0; i < array.size(); i++) {
                  missingStereotypes.remove(array.getString(i));
               }
               accept = missingStereotypes.isEmpty();
               break;
            case STRING:
               accept = stereotypes.contains(((JsonString) value).getString()) && stereotypes.size() == 1;
               break;
            default:
               accept = false;
               break;
         }
      }
      return accept;
   }
}
