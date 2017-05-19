package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IModelField {

  String getName();

  IData getType();

  ModelFieldCardinality getCardinality();

  IMetadata getMetadata();

  IModel getParentModel();
}
