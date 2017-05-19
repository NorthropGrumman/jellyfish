package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;

public interface IDataReferenceField extends IReferenceField {

  IData getType();

  ModelFieldCardinality getCardinality();
}
