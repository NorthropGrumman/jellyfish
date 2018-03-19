package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;

public class SelfInitializingProperties {

   /**
//    * The supplier that will supply the list to wrap when the first element is added to this collection.
//    */
//   private final Supplier<EList<PropertyFieldDeclaration>> initializer;
//
//   /**
//    * If true, the first element has been added to this collection and this list is now wrapping the list returned from
//    * the supplier.
//    */
//   private boolean hasInitialized = false;
//
//   public SelfInitializingProperties(Function<PropertyFieldDeclaration, IProperty> wrapperFunction,
//                                     Function<IProperty, PropertyFieldDeclaration> unwrapperFunction,
//                                     Function<PropertyFieldDeclaration, String> namingFunction,
//                                     Supplier<EList<PropertyFieldDeclaration>> initializer) {
//      // Just past an empty list to the super class for now.  We'll replace it before the first add so it
//      // will never actually contain anything.
//      //super(ECollections.emptyEList(), wrapperFunction, unwrapperFunction, namingFunction);
//      this.initializer = Preconditions.checkNotNull(initializer, "initializer may not be null!");
//   }
//
//   @Override
//   public boolean add(IProperty t) {
//      // Is this the first add?
//      if (!hasInitialized) {
//         hasInitialized = true;
//         // Start wrapping the supplied  list.
//         setWrapped(initializer.get());
//      }
//      return super.add(t);
//   }

}
