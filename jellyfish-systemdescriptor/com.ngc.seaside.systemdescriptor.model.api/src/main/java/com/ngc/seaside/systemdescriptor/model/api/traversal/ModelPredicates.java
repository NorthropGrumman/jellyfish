/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
