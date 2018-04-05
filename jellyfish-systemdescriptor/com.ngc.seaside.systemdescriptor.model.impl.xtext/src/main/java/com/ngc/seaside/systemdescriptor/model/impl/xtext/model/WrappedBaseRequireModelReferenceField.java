package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Optional;

/**
 * Adapts an {@link BaseRequireDeclaration} to an {@link IModelReferenceField}.
 * This class is not threadsafe.
 */
public class WrappedBaseRequireModelReferenceField
      extends AbstractWrappedModelReferenceField<BaseRequireDeclaration, WrappedBaseRequireModelReferenceField> {

   public WrappedBaseRequireModelReferenceField(IWrapperResolver resolver, BaseRequireDeclaration wrapped) {
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
    * Creates a new {@code BaseRequireDeclaration} from the given field.
    */
   public static BaseRequireDeclaration toXTextRequireDeclaration(IWrapperResolver resolver,
                                                                  IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      BaseRequireDeclaration d = SystemDescriptorFactory.eINSTANCE.createBaseRequireDeclaration();
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
