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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Optional;

/**
 * Adapts an {@link PartDeclaration} to an {@link IModelReferenceField}.
 * This class is not threadsafe.
 */
public class WrappedBasePartModelReferenceField
      extends AbstractWrappedModelReferenceField<BasePartDeclaration, WrappedBasePartModelReferenceField> {

   public WrappedBasePartModelReferenceField(IWrapperResolver resolver, BasePartDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IModel getType() {
      return resolver.getWrapperFor(wrapped.getType());
   }

   @Override
   public IModelReferenceField setType(IModel type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type.getParent() != null, "data must be contained within a package");
      wrapped.setType(findXtextModel(type.getName(), type.getParent().getName()));
      return this;
   }

   /**
    * Creates a new {@code BasePartDeclaration} from the given field.
    */
   public static BasePartDeclaration toXTextPartDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      BasePartDeclaration d = SystemDescriptorFactory.eINSTANCE.createBasePartDeclaration();
      d.setName(field.getName());
      d.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, field.getMetadata(), null));
      d.setType(doFindXtextModel(resolver, field.getType().getName(), field.getType().getParent().getName()));
      return d;
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      return Optional.empty();
   }
}
