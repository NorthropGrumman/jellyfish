package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

public abstract class AbstractWrappedModelReferenceField<T extends FieldDeclaration> extends AbstractWrappedXtext<T>
    implements IModelReferenceField {

  public AbstractWrappedModelReferenceField(IWrapperResolver resolver, T wrapped) {
    super(resolver, wrapped);
  }

  @Override
  public IMetadata getMetadata() {
    // TODO TH: metadata on fields not currently supported.
    return IMetadata.EMPTY_METADATA;
  }

  @Override
  public IReferenceField setMetadata(IMetadata metadata) {
    // TODO TH: metadata on fields not currently supported.
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IModel getParent() {
    return resolver.getWrapperFor((Model) wrapped.eContainer().eContainer());
  }

  Model findXtextModel(String name, String packageName) {
    return doFindXtextModel(resolver, name, packageName);
  }

  static Model doFindXtextModel(IWrapperResolver resolver, String name, String packageName) {
    return resolver.findXTextModel(name, packageName).orElseThrow(() -> new IllegalStateException(String.format(
        "Could not find XText type for model type '%s' in package '%s'!"
        + "  Make sure the IModel object is added to"
        + " a package within the ISystemDescriptor before adding a reference to it!",
        name,
        packageName)));
  }
}
