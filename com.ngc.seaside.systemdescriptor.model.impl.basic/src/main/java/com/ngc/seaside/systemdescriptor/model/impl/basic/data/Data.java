package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.Objects;

/**
 * Implementation of the interface IData.  Stores IDataField objects and metadata.
 * 
 * @author psnell
 *
 */
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

  @Override
  public String getFullyQualifiedName() {
    return String.format("%s%s%s",
                         parent == null ? "" : parent.getName(),
                         parent == null ? "" : ".",
                         name);
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
           parent == data.parent &&
           Objects.equals(metadata, data.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields, name, System.identityHashCode(parent), metadata);
  }

  @Override
  public String toString() {
    return "Data[" +
           "fields=" + fields +
           ", name='" + name + '\'' +
           ", parent=" + (parent == null ? "null" : parent.getName()) +
           ", metadata=" + metadata +
           ']';
  }

}
