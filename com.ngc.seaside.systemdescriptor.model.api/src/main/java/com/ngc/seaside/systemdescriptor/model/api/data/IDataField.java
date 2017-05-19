package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IDataField {

  String getName();

  DataTypes getType();

  IMetadata getMetadata();

  IData getParentData();
}
