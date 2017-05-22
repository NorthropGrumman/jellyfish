package com.ngc.seaside.systemdescriptor.model.api.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;

public interface IModelLink<T extends IReferenceField> {

  T getSource();

  IModelLink<T> setSource(T source);

  T getTarget();

  IModelLink<T> setTarget(T target);

  IModel getParent();
}
