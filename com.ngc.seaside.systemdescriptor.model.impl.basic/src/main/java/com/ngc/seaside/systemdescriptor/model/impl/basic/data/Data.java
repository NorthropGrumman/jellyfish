package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;

public class Data implements IData {

  protected final INamedChildCollection<IData, IDataField> fields;
  protected final String name;
  protected IPackage parent;
  protected IMetadata metadata;

  private Data(String name, INamedChildCollection<IData, IDataField> fields) {
    this.name = name;
    this.fields = fields;
  }

  public Data(String name) {
    Preconditions.checkNotNull(name, "name may not be null!");
    Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
    this.name = name;
    this.fields = new NamedChildCollection<IData, IDataField>()
        .setOnChildAdded(field -> {
          if (field instanceof DataField) {
            DataField casted = (DataField) field;
            casted.setParent(Data.this);
          }
        })
        .setOnChildRemoved(field -> {
          if (field instanceof DataField) {
            DataField casted = (DataField) field;
            casted.setParent(null);
          }
        });
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public IPackage getParent() {
    return parent;
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IData setMetadata(IMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  @Override
  public INamedChildCollection<IData, IDataField> getFields() {
    return fields;
  }

  public Data setParent(IPackage parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Data)) {
      return false;
    }
    Data data = (Data) o;
    return Objects.equals(fields, data.fields) &&
           Objects.equals(name, data.name) &&
           Objects.equals(parent, data.parent) &&
           Objects.equals(metadata, data.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields, name, parent, metadata);
  }

  @Override
  public String toString() {
    return "Data[" +
           "fields=" + fields +
           ", name='" + name + '\'' +
           ", parent=" + parent +
           ", metadata=" + metadata +
           ']';
  }

  public static IData immutable(IData data) {
    ImmutableData immutable = new ImmutableData(data.getName(),
                                                NamedChildCollection.immutable(data.getFields()));
    immutable.parent = data.getParent();
    immutable.metadata = Metadata.immutable(data.getMetadata());
    return immutable;
  }

  private static class ImmutableData extends Data {

    private ImmutableData(String name, INamedChildCollection<IData, IDataField> fields) {
      super(name, fields);
    }

    @Override
    public IData setMetadata(IMetadata metadata) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public Data setParent(IPackage parent) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }
  }
}
