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
package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Objects;

import javax.json.JsonObject;

/**
 * Implements an IMetadata interface.  Stores a JsonObject.
 *
 * @author psnell
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
      return "Metadata["
             + "json='"
             + (json == null ? "null" : json)
             + ']';
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
