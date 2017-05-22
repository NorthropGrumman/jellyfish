package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public class DataField implements IDataField {

  @Override
  public String getName() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IData getParent() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public DataTypes getType() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IDataField setType(DataTypes type) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IMetadata getMetadata() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IDataField setMetdata(IMetadata metadata) {
    throw new UnsupportedOperationException("not implemented");
  }
}
