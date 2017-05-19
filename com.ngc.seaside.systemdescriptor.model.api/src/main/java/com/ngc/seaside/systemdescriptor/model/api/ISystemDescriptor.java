package com.ngc.seaside.systemdescriptor.model.api;

import java.util.Set;

public interface ISystemDescriptor {

  Set<IPackage> getPackages();

  // Optional<Model> findModel(String fqn)
  // Optional<Data> findData(String fqn)
}
