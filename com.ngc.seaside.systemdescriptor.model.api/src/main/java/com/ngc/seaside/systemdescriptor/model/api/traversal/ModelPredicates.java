package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

public class ModelPredicates {

   public final static String STEREOTYPE_MEMBER_NAME = "stereotypes";

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

   public static Predicate<IModel> withAllStereotypes(Collection<String> stereotypes) {
      if (stereotypes == null) {
         throw new NullPointerException("stereotypes may not be null!");
      }
      return m -> containsAllStereotypes(m, stereotypes);
   }

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
         JsonValue value = metadata.getJson().getValue("/" + STEREOTYPE_MEMBER_NAME);
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
         }
      }
      return accept;
   }

   private static boolean containsAllStereotypes(IModel model, Collection<String> stereotypes) {
      IMetadata metadata = model.getMetadata();
      boolean accept = metadata != null;
      if (accept) {
         JsonValue value = metadata.getJson().getValue("/" + STEREOTYPE_MEMBER_NAME);
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
         }
      }
      return accept;
   }
}
