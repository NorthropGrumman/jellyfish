package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

public interface ISystemDescriptorService {

  // result parseProject(Path projectDirectory);

  // result parseFiles(Collection<Path> paths);

  ISystemDescriptor immutableCopy(ISystemDescriptor descriptor);

}
