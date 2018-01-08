package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link PartDeclaration} to an {@link IModelReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedPartModelReferenceField extends AbstractWrappedModelReferenceField<PartDeclaration, WrappedPartModelReferenceField> {

   public WrappedPartModelReferenceField(IWrapperResolver resolver, PartDeclaration wrapped) {
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
    * Creates a new {@code PartDeclaration} from the given field.
    */
   public static PartDeclaration toXTextPartDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      PartDeclaration d = SystemDescriptorFactory.eINSTANCE.createPartDeclaration();
      d.setName(field.getName());
      d.setMetadata(WrappedMetadata.toXtextJson(field.getMetadata()));
      d.setType(doFindXtextModel(resolver, field.getType().getName(), field.getType().getParent().getName()));
      return d;
   }
}
