package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

/**
 * Base class for types that adapts requirements or part field declarations to {@link IModelReferenceField}s.
 *
 * This class is not threadsafe.
 *
 * @param <T> the type of XText field this class is wrapping
 * @param <I> the type of the class implementing this class
 */
public abstract class AbstractWrappedModelReferenceField<T extends FieldDeclaration, I extends AbstractWrappedModelReferenceField<T, I>> extends AbstractWrappedXtext<T>
      implements IModelReferenceField {

   public AbstractWrappedModelReferenceField(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IMetadata getMetadata() {
      return WrappedMetadata.fromXtextJson(wrapped.getMetadata());
   }

   @SuppressWarnings("unchecked")
   @Override
   public I setMetadata(IMetadata metadata) {
      wrapped.setMetadata(WrappedMetadata.toXtextJsonObject(metadata.getJson()));
      return (I) this;
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IModel getParent() {
      return resolver.getWrapperFor((Model) wrapped.eContainer().eContainer());
   }

   /**
    * Finds the XText {@code Model} object with the given name and package.
    *
    * @param name        the name of the of type
    * @param packageName the name of the package that contains the type
    * @return the XText type
    * @throws IllegalStateException if the XText type could not be found
    */
   Model findXtextModel(String name, String packageName) {
      return doFindXtextModel(resolver, name, packageName);
   }

   /**
    * Finds the XText {@code Model} object with the given name and package.
    *
    * @param resolver    the resolver that can location XText data objects
    * @param name        the name of the of type
    * @param packageName the name of the package that contains the type
    * @return the XText type
    * @throws IllegalStateException if the XText type could not be found
    */
   static Model doFindXtextModel(IWrapperResolver resolver, String name, String packageName) {
      return resolver.findXTextModel(name, packageName).orElseThrow(() -> new IllegalStateException(String.format(
            "Could not find XText type for model type '%s' in package '%s'!"
            + "  Make sure the IModel object is added to"
            + " a package within the ISystemDescriptor before adding a reference to it!",
            name,
            packageName)));
   }
}
