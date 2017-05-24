package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataType;

public class WrappedDataField extends AbstractWrappedXtext<DataFieldDeclaration> implements IDataField {

  private final IData parent;
  private IMetadata metadata;

  public WrappedDataField(DataFieldDeclaration wrapped, IData parent) {
    super(wrapped);
    this.parent = Preconditions.checkNotNull(parent, "parent may not be null!");
    this.metadata = WrappedMetadata.fromXtextJson(wrapped.getMetadata());
  }

  @Override
  public DataTypes getType() {
    return DataTypes.valueOf(wrapped.getType().name());
  }

  @Override
  public IDataField setType(DataTypes type) {
    Preconditions.checkNotNull(type, "type may not be null!");
    wrapped.setType(DataType.valueOf(type.name()));
    return this;
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IDataField setMetadata(IMetadata metadata) {
    Preconditions.checkNotNull(metadata, "metadata may not be null!");
    this.metadata = metadata;
    wrapped.setMetadata(WrappedMetadata.toXtextJson(metadata));
    return this;
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IData getParent() {
    return parent;
  }
}
