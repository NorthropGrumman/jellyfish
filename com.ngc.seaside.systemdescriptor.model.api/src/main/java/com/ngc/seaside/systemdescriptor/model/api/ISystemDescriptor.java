package com.ngc.seaside.systemdescriptor.model.api;

import java.util.Set;

public interface ISystemDescriptor {

  Set<IPackage> getPackages();

  // findModel(String fqn)
  // findData(String fqn)
}
