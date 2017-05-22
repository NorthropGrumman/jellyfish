package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class SystemDescriptorPackage implements IPackage {

  @Override
  public String getName() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public ISystemDescriptor getParent() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IPackage, IData> getData() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IPackage, IModel> getModels() {
    throw new UnsupportedOperationException("not implemented");
  }
}
