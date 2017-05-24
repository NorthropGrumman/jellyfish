package com.ngc.seaside.systemdescriptor.model.api.metadata;

import javax.json.JsonObject;

/**
 * Describes metadata and information that may be associated with different modeling elements.  Metadata is formatted in
 * JSON.  Operations that change the state of this object may throw {@code UnsupportedOperationException}s if the object
 * is immutable.
 */
public interface IMetadata {

  /**
   * Gets the JSON object that is the metadata.
   *
   * @return the JSON object that is the metadata
   */
  JsonObject getJson();

  /**
   * Sets the JSON object that is the metadata.
   *
   * @param json the JSON object that is the metadata
   * @return the JSON object that is the metadata
   */
  IMetadata setJson(JsonObject json);
}
