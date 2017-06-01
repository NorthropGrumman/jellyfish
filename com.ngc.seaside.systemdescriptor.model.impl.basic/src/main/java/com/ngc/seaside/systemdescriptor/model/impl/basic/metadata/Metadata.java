package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import java.util.Objects;

import javax.json.JsonObject;

/**
 * Implements an IMetadata interface.  Stores a JsonObject.
 * 
 * @author psnell
 *
 */
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Metadata)) {
      return false;
    }
    Metadata data = (Metadata) o;
    return Objects.equals(json, data.json);
  }

  @Override
  public int hashCode() {
    return Objects.hash(json);
  }

  @Override
  public String toString() {
    return "Metadata[" +
           "json='" + (json == null ? "null" : json) +
           ']';
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
