package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IData extends INamedChild<IPackage> {

  IMetadata getMetadata();

  IData setMetadata(IMetadata metadata);

  INamedChildCollection<IData, IDataField> getFields();

  String getFullyQualifiedName();
}
