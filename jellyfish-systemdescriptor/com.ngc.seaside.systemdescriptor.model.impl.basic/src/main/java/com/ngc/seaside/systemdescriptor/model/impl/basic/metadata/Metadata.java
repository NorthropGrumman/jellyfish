/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
