package com.ngc.seaside.systemdescriptor.model.api;

import java.util.Collection;
import java.util.Optional;

public interface INamedChildCollection<P, T extends INamedChild<P>> extends Collection<T> {

  Optional<T> getByName(String name);
}
