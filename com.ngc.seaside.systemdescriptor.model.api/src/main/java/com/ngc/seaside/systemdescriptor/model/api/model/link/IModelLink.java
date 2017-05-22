package com.ngc.seaside.systemdescriptor.model.api.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;

public interface IModelLink<T extends IReferenceField> {

  T getSource();

  T getTarget();

  IModel getParent();
}
