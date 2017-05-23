package com.ngc.seaside.systemdescriptor.model.impl.basic.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.ModelFieldCardinality;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;

public class DataReferenceField implements IDataReferenceField {

  protected final String name;
  protected IMetadata metadata;
  protected IData type;
  protected ModelFieldCardinality cardinality = ModelFieldCardinality.SINGLE;
  protected IModel parent;

  public DataReferenceField(String name) {
    Preconditions.checkNotNull(name, "name may not be null!");
    Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public IModel getParent() {
    return parent;
  }

  @Override
  public IData getType() {
    return type;
  }

  @Override
  public IDataReferenceField setType(IData type) {
    this.type = type;
    return this;
  }

  @Override
  public ModelFieldCardinality getCardinality() {
    return cardinality;
  }

  @Override
  public IDataReferenceField setCardinality(ModelFieldCardinality cardinality) {
    Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
    this.cardinality = cardinality;
    return this;
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IReferenceField setMetadata(IMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  public DataReferenceField setParent(IModel model) {
    this.parent = parent;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DataReferenceField)) {
      return false;
    }
    DataReferenceField that = (DataReferenceField) o;
    return Objects.equals(name, that.name) &&
           Objects.equals(metadata, that.metadata) &&
           Objects.equals(type, that.type) &&
           cardinality == that.cardinality &&
           parent == that.parent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, metadata, type, cardinality, System.identityHashCode(parent));
  }

  @Override
  public String toString() {
    return "DataReferenceField[" +
           "name='" + name + '\'' +
           ", metadata=" + metadata +
           ", type=" + type +
           ", cardinality=" + cardinality +
           ", parent=" + (parent == null ? "null" : parent.getName()) +
           ']';
  }

  public static IDataReferenceField immutable(IDataReferenceField field) {
    Preconditions.checkNotNull(field, "field may not be null!");

    // Get the immutable data type.
    ISystemDescriptor descriptor = field.getParent().getParent().getParent();


    ImmutableDataReferenceField immutable = new ImmutableDataReferenceField(field.getName());
    immutable.metadata = Metadata.immutable(field.getMetadata());
    immutable.type = field.getType();
    immutable.cardinality = field.getCardinality();
    immutable.parent = field.getParent();
    return immutable;
  }

  private static class ImmutableDataReferenceField extends DataReferenceField {

    private ImmutableDataReferenceField(String name) {
      super(name);
    }

    @Override
    public IDataReferenceField setType(IData type) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public IDataReferenceField setCardinality(ModelFieldCardinality cardinality) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public IReferenceField setMetadata(IMetadata metadata) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }

    @Override
    public DataReferenceField setParent(IModel model) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }
  }
}
