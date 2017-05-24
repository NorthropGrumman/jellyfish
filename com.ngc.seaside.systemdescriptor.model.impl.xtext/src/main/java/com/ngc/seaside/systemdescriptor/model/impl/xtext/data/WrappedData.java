package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

public class WrappedData extends AbstractWrappedXtext<Data> implements IData {

  private IMetadata metadata;

  public WrappedData(IWrapperResolver resolver, Data wrapped) {
    super(resolver, wrapped);
    this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IData setMetadata(IMetadata metadata) {
    Preconditions.checkNotNull(metadata, "metadata may not be null!");
    this.metadata = metadata;
    wrapped.setMetadata(WrappedMetadata.toXtext(metadata));
    return this;
  }

  @Override
  public INamedChildCollection<IData, IDataField> getFields() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public String getFullyQualifiedName() {
    Package p = (Package) wrapped.eContainer();
    return String.format("%s%s%s",
                         p == null ? "" : p.getName(),
                         p == null ? "" : ".",
                         wrapped.getName());
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IPackage getParent() {
    return resolver.getWrapperFor((Package) wrapped.eContainer());
  }
}
