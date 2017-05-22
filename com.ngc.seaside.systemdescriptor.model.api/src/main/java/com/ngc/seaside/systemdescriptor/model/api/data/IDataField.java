package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IDataField extends INamedChild<IData> {

  DataTypes getType();

  IDataField setType(DataTypes type);

  IMetadata getMetadata();

  IDataField setMetdata(IMetadata metadata);
}
