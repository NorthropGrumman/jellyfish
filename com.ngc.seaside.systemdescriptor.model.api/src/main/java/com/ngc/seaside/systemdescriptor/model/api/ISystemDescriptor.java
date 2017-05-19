package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Optional;
import java.util.Set;

public interface ISystemDescriptor {

  Set<IPackage> getPackages();

  Optional<IModel> findModel(String fullyQualifiedName);

  Optional<IData> findData(String fullyQualifiedName);
}
