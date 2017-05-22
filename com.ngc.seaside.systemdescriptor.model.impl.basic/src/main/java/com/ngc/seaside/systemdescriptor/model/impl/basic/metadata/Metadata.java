package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.json.JsonObject;

public class Metadata implements IMetadata {

  protected final Collection<JsonObject> jsonObjects;

  public Metadata() {
    this(new ArrayList<>());
  }

  public Metadata(Collection<JsonObject> jsonObjects) {
    this.jsonObjects = jsonObjects;
  }

  @Override
  public Collection<JsonObject> getJsonObjects() {
    return jsonObjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Metadata)) {
      return false;
    }
    Metadata metadata = (Metadata) o;
    return Objects.equals(jsonObjects, metadata.jsonObjects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jsonObjects);
  }

  @Override
  public String toString() {
    return "Metadata[" +
           "jsonObjects=" + jsonObjects +
           ']';
  }

  public static IMetadata immutable(IMetadata metadata) {
    return new Metadata(Collections.unmodifiableCollection(metadata.getJsonObjects()));
  }
}
