package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.List;

public interface IModel {

  String getName();

  IMetadata getMetadata();

  List<IModelField> getInputs();

  List<IModelField> getOutputs();

  IPackage getParentPackage();
}
