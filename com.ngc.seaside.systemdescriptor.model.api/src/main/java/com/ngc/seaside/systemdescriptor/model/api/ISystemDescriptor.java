package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traveral.IVisitor;

import java.util.Optional;

public interface ISystemDescriptor {

  INamedChildCollection<ISystemDescriptor, IPackage> getPackages();

  Optional<Object> traverse(IVisitor visitor);

  Optional<IModel> findModel(String fullyQualifiedName);

  Optional<IModel> findModel(String packageName, String name);

  Optional<IData> findData(String fullyQualifiedName);

  Optional<IData> findData(String packageName, String name);
}
