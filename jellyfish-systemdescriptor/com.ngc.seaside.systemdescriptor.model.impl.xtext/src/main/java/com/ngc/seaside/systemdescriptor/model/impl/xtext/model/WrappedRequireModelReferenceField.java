package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link RequireDeclaration} to an {@link IModelReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedRequireModelReferenceField extends AbstractWrappedModelReferenceField<RequireDeclaration, WrappedRequireModelReferenceField> {

   public WrappedRequireModelReferenceField(IWrapperResolver resolver, RequireDeclaration wrapped) {
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
    * Creates a new {@code RequireDeclaration} from the given field.
    */
   public static RequireDeclaration toXTextRequireDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      RequireDeclaration d = SystemDescriptorFactory.eINSTANCE.createRequireDeclaration();
      d.setName(field.getName());
      d.setDefinition(WrappedDeclarationDefinition.toXtext(field.getMetadata()));
      d.setType(doFindXtextModel(resolver, field.getType().getName(), field.getType().getParent().getName()));
      return d;
   }
}
