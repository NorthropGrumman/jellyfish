package com.ngc.seaside.systemdescriptor.model.api.metadata;

import javax.json.JsonObject;

/**
 * Describes metadata and information that may be associated with different modeling elements.  Metadata is formatted in
 * JSON.  Operations that change the state of this object may throw {@code UnsupportedOperationException}s if the object
 * is immutable.
 */
public interface IMetadata {

  /**
   * An immutable singleton that is used to indicate empty or missing metadata.
   */
  IMetadata EMPTY_METADATA = new IMetadata() {
    @Override
    public JsonObject getJson() {
      return JsonObject.EMPTY_JSON_OBJECT;
    }

    @Override
    public IMetadata setJson(JsonObject json) {
      throw new UnsupportedOperationException("this object may not be modified!");
    }
  };

  /**
   * Gets the JSON object that is the metadata.
   *
   * @return the JSON object that is the metadata (never {@code null})
   */
  JsonObject getJson();

  /**
   * Sets the JSON object that is the metadata.  Use {@link #EMPTY_METADATA} to set empty metadata on an object.
   *
   * @param json the JSON object that is the metadata
   * @return the JSON object that is the metadata
   * @throws NullPointerException if {@code json} is {@code null}
   */
  IMetadata setJson(JsonObject json);
}
