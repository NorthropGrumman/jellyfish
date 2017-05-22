package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;

public class DataField implements IDataField {

  protected final String name;
  protected IData parent;
  protected DataTypes type;
  protected IMetadata metadata;

  public DataField(String name) {
    Preconditions.checkNotNull(name, "name may not be null!");
    Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public IData getParent() {
    return parent;
  }

  @Override
  public DataTypes getType() {
    return type;
  }

  @Override
  public IDataField setType(DataTypes type) {
    this.type = type;
    return this;
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IDataField setMetadata(IMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  public DataField setParent(IData parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DataField)) {
      return false;
    }
    DataField dataField = (DataField) o;
    return Objects.equals(name, dataField.name) &&
           Objects.equals(parent, dataField.parent) &&
           type == dataField.type &&
           Objects.equals(metadata, dataField.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, parent, type, metadata);
  }

  @Override
  public String toString() {
    return "DataField[" +
           "name='" + name + '\'' +
           ", parent=" + parent +
           ", type=" + type +
           ", metadata=" + metadata +
           ']';
  }

  public static IDataField immutable(IDataField dataField) {
    ImmutableDataField immutable = new ImmutableDataField(dataField.getName());
    immutable.parent = dataField.getParent();
    immutable.type = dataField.getType();
    immutable.metadata = Metadata.immutable(dataField.getMetadata());
    return immutable;
  }

  static class ImmutableDataField extends DataField {

    private ImmutableDataField(String name) {
      super(name);
    }

    @Override
    public IDataField setType(DataTypes type) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public IDataField setMetadata(IMetadata metadata) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public DataField setParent(IData parent) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }
  }
}
