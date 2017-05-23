package com.ngc.seaside.systemdescriptor.model.api.metadata;

import java.util.Collection;

import javax.json.JsonObject;

/**
 * Describes metadata and information that may be associated with different modeling elements.  Metadata is formatted in
 * JSON.  Operations that change the state of this object may throw {@code UnsupportedOperationException}s if the object
 * is immutable.
 */
public interface IMetadata {

  /**
   * Gets a collection of JSON objects that describe the metadata of an attached object.  The returned collection may
   * not be modifiable if this object is immutable.
   *
   * @return a collection of JSON objects
   */
  Collection<JsonObject> getJsonObjects();
}
