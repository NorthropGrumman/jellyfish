package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedPartModelReferenceField extends AbstractWrappedModelReferenceField<PartDeclaration> {

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

  public static PartDeclaration toXTextPartDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
    Preconditions.checkNotNull(resolver, "resolver may not be null!");
    Preconditions.checkNotNull(field, "field may not be null!");
    PartDeclaration d = SystemDescriptorFactory.eINSTANCE.createPartDeclaration();
    d.setName(field.getName());
    d.setType(doFindXtextModel(resolver, field.getType().getName(), field.getType().getParent().getName()));
    return d;
  }
}
