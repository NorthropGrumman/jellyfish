package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DeferredInitialization {

   private final static Collection<IDeferredInitialization> deferredComponents =
         Collections.synchronizedCollection(new ArrayList<>());

   private DeferredInitialization() {
   }

   public static void register(IDeferredInitialization component) {
      deferredComponents.add(Preconditions.checkNotNull(component, "component may not be null!"));
   }

   public static void packagesWrapped() {
      synchronized (deferredComponents) {
         deferredComponents.forEach(IDeferredInitialization::postPackagesWrapped);
         deferredComponents.clear();
      }
   }

   public interface IDeferredInitialization {

      void postPackagesWrapped();
   }
}
