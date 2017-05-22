package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

public interface IReferenceField extends INamedChild<IModel> {

  IMetadata getMetadata();

  IReferenceField setMetdata(IMetadata metadata);
}
