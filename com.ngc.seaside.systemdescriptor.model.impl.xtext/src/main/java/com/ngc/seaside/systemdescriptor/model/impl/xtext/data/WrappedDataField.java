package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataType;

import java.util.Objects;

public class WrappedDataField implements IDataField {

  private final DataFieldDeclaration wrapped;
  private final IData parent;

  public WrappedDataField(DataFieldDeclaration wrapped, IData parent) {
    this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
    this.parent = Preconditions.checkNotNull(parent, "parent may not be null!");
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
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IDataField setMetadata(IMetadata metadata) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IData getParent() {
    return parent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WrappedDataField)) {
      return false;
    }
    WrappedDataField dataField = (WrappedDataField) o;
    return Objects.equals(wrapped, dataField.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }

  @Override
  public String toString() {
    return wrapped.toString();
  }
}
