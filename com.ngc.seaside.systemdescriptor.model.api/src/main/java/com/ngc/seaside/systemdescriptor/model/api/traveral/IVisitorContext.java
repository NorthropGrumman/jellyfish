package com.ngc.seaside.systemdescriptor.model.api.traveral;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

public interface IVisitorContext<T> {

  ISystemDescriptor getSystemDescriptor();

  T getResult();

  void setResult(T result);

  void stop();
}
