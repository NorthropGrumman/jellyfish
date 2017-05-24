package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import java.nio.file.Path;
import java.util.Collection;

public interface ISystemDescriptorService {

  IParsingResult parseProject(Path projectDirectory);

  IParsingResult parseFiles(Collection<Path> paths);

  ISystemDescriptor immutableCopy(ISystemDescriptor descriptor);

}
