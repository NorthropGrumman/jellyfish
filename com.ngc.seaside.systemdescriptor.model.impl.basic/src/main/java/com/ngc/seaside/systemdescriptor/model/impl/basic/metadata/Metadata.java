package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import javax.json.JsonObject;

public class Metadata implements IMetadata {

  private JsonObject json;

  @Override
  public JsonObject getJson() {
    return json;
  }

  @Override
  public IMetadata setJson(JsonObject json) {
    this.json = json;
    return this;
  }

  public static IMetadata immutable(IMetadata metadata) {
    Preconditions.checkNotNull(metadata, "metadata may not be null!");
    return new ImmutableMetadata(metadata.getJson());
  }

  private static class ImmutableMetadata extends Metadata {

    private ImmutableMetadata(JsonObject json) {
      super.setJson(json);
    }

    @Override
    public IMetadata setJson(JsonObject json) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }
  }
}
