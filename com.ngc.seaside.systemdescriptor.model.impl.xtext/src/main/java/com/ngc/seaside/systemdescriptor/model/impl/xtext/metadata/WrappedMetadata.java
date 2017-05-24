package com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;

import java.util.Collection;

import javax.json.JsonObject;

public class WrappedMetadata implements IMetadata {

  public WrappedMetadata(Metadata metadata) {
    Preconditions.checkNotNull(metadata, "metadata may not be null!");
  }

  @Override
  public Collection<JsonObject> getJsonObjects() {
    throw new UnsupportedOperationException("not implemented");
  }
}
