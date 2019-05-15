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
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Value;

import org.eclipse.xtext.util.ITextRegion;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

class JsonValueChainedMethodCall extends AbstractChainedMethodCall<JsonValue> {

   private final JsonValue json;
   private final Value xtextJson;

   protected JsonValueChainedMethodCall(JsonValue json,
                                        Value xtextJson,
                                        ChainedMethodCallContext context) {
      super(json, context);
      this.json = json;
      this.xtextJson = xtextJson;
      try {
         register(JsonValue.class.getMethod("asJsonObject"), this::thenAsJsonObject);
         register(JsonValue.class.getMethod("asJsonArray"), this::thenAsJsonArray);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<JsonObject> thenAsJsonObject() {
      return new JsonObjectChainedMethodCall(json.asJsonObject(),
               (com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject) xtextJson, context);
   }

   private IChainedMethodCall<JsonArray> thenAsJsonArray() {
      return new JsonArrayChainedMethodCall(json.asJsonArray(), (ArrayValue) xtextJson, context);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextJson);
      return context.getSourceLocation(xtextJson, region);
   }

}
