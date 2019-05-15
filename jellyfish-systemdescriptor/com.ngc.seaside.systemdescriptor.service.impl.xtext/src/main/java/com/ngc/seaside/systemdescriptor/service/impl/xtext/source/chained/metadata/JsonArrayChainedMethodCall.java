/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.metadata;

import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Value;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.util.ITextRegion;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

class JsonArrayChainedMethodCall extends AbstractChainedMethodCall<JsonArray> {

   private final JsonArray json;
   private final com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue xtextJson;

   protected JsonArrayChainedMethodCall(JsonArray json,
                                        com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue xtextJson,
                                        ChainedMethodCallContext context) {
      super(json, context);
      this.json = json;
      this.xtextJson = xtextJson;
      try {
         register(JsonArray.class.getMethod("get", int.class), this::thenGet);
         register(JsonArray.class.getMethod("getString", int.class), this::thenGetString);
         register(JsonArray.class.getMethod("getJsonObject", int.class), this::thenGetJsonObject);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<JsonValue> thenGet(int index) {
      return new JsonValueChainedMethodCall(json.get(index), getValue(index), context);
   }

   private IChainedMethodCall<JsonObject> thenGetJsonObject(int index) {
      Value value = getValue(index);
      if (!(value instanceof com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject)) {
         throw new IllegalStateException("value at index " + index + " is not a json object");
      }
      return new JsonObjectChainedMethodCall(json.getJsonObject(index),
               (com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject) value, context);
   }

   private IChainedMethodCall<String> thenGetString(int index) {
      Value value = getValue(index);
      if (!(value instanceof StringValue)) {
         throw new IllegalStateException("value at index " + index + " is not a json string");
      }
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(value);
      IDetailedSourceLocation location = context.getSourceLocation(value, region);
      location = location.getSubLocation(1, location.getLength() - 2);
      return new StringChainedMethodCall(location, context);
   }

   private Value getValue(int index) {
      EList<Value> values = xtextJson.getValue().getValues();
      if (index < 0 || index >= values.size()) {
         throw new IllegalArgumentException("Invalid index for json array " + json + ": " + index);
      }
      return values.get(index);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextJson);
      return context.getSourceLocation(xtextJson, region);
   }

}
