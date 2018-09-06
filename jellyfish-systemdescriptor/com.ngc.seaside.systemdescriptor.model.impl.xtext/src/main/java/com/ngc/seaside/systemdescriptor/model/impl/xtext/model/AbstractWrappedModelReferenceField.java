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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

/**
 * Base class for types that adapts requirements or part field declarations to {@link IModelReferenceField}s.
 * This class is not threadsafe.
 *
 * @param <T> the type of XText field this class is wrapping
 * @param <I> the type of the class implementing this class
 */
public abstract class AbstractWrappedModelReferenceField
      <T extends FieldDeclaration, I extends AbstractWrappedModelReferenceField<T, I>>
      extends AbstractWrappedXtext<T>
      implements IModelReferenceField {

   private IMetadata metadata;
   private IProperties properties;

   /**
    * Creates a new field.
    */
   public AbstractWrappedModelReferenceField(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedDeclarationDefinition.metadataFromXtext(wrapped.getDefinition());
      this.properties = WrappedDeclarationDefinition.propertiesFromXtext(resolver, wrapped.getDefinition());
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @SuppressWarnings("unchecked")
   @Override
   public I setMetadata(IMetadata metadata) {
      wrapped.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, metadata, getProperties()));
      this.metadata = metadata;
      return (I) this;
   }

   @Override
   public IProperties getProperties() {
      return properties;
   }

   @SuppressWarnings("unchecked")
   @Override
   public I setProperties(IProperties properties) {
      wrapped.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, getMetadata(), properties));
      this.properties = properties;
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
