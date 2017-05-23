package com.ngc.seaside.systemdescriptor.model.api.traveral;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

public interface IVisitorContext {

  ISystemDescriptor getSystemDescriptor();

  Object getResult();

  void setResult(Object result);

  void stop();
}
