package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.List;

public interface IData {

  String getName();

  IMetadata getMetadata();

  List<IDataField> getFields();

  IPackage getParentPackage();
}
