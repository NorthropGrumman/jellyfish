package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Set;

public interface IPackage {

  INamedChildCollection<IPackage, IData> getData();

  INamedChildCollection<IPackage, IModel> getModels();

}
