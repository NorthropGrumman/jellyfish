package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.function.Predicate;

public class ModelPredicates {

   public static Predicate<IModel> withAllStereotype(String stereotype, String... stereotypes) {
      throw new UnsupportedOperationException("not implemented");
   }

   public static Predicate<IModel> withAllStereotype(Collection<String> stereotypes) {
      throw new UnsupportedOperationException("not implemented");
   }

   public static Predicate<IModel> withAnyStereotype(String stereotype, String... stereotypes) {
      throw new UnsupportedOperationException("not implemented");
   }

   public static Predicate<IModel> withAnyStereotype(Collection<String> stereotypes) {
      throw new UnsupportedOperationException("not implemented");
   }
}
