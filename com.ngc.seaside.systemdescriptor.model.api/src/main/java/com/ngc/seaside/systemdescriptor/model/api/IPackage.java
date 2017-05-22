package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface IPackage extends INamedChild<ISystemDescriptor> {

  INamedChildCollection<IPackage, IData> getData();

  INamedChildCollection<IPackage, IModel> getModels();
}
