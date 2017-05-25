package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

public abstract class AbstractWrappedDataReferenceField<T extends FieldDeclaration> extends AbstractWrappedXtext<T>
    implements IDataReferenceField {

  public AbstractWrappedDataReferenceField(IWrapperResolver resolver, T wrapped) {
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

  protected Data findXtextData(String name, String packageName) {
    return doFindXtextData(resolver, name, packageName);
  }

  protected static Data doFindXtextData(IWrapperResolver resolver, String name, String packageName) {
    return resolver.findXTextData(name, packageName).orElseThrow(() -> new IllegalStateException(String.format(
        "Could not find XText type for data type '%s' in package '%s'!"
        + "  Make sure the IData object is added to"
        + " a package within the ISystemDescriptor before adding a reference to it!",
        name,
        packageName)));
  }
}
