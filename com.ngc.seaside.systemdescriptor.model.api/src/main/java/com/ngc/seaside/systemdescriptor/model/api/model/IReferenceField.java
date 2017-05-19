package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IReferenceField {

  String getName();

  IMetadata getMetadata();

  IModel getParentModel();

}
