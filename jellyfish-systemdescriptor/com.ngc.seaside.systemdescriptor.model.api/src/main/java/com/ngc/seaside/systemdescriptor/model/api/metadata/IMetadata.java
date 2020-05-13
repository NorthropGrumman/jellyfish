/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
