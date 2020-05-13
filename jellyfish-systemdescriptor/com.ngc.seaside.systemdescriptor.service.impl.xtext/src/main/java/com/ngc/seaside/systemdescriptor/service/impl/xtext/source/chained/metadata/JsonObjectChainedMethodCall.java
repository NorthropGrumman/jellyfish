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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.metadata;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.TerminatingChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Value;

import org.eclipse.xtext.util.ITextRegion;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

class JsonObjectChainedMethodCall extends AbstractChainedMethodCall<JsonObject> {

   private final JsonObject json;
   private final com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject xtextJson;

   protected JsonObjectChainedMethodCall(JsonObject json,
                                         com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject xtextJson,
                                         ChainedMethodCallContext context) {
      super(json, context);
      this.json = json;
      this.xtextJson = xtextJson;
      try {
         register(Map.class.getMethod("get", Object.class), this::thenGet);
         register(Map.class.getMethod("containsKey", Object.class), this::thenContainsKey);
         register(JsonObject.class.getMethod("getString", String.class), this::thenGetString);
         register(JsonObject.class.getMethod("getJsonObject", String.class), this::thenGetJsonObject);
         register(JsonObject.class.getMethod("getJsonArray", String.class), this::thenGetJsonArray);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<JsonValue> thenGet(String key) {
      Member member = getMember(key);
      return new JsonValueChainedMethodCall(json.get(key), member.getValue(), context);
   }

   private IChainedMethodCall<Boolean> thenContainsKey(String key) {
      Member member = getMember(key);
      ITextRegion region = context.getLocationInFileProvider().getSignificantTextRegion(member,
               SystemDescriptorPackage.Literals.MEMBER__KEY, 0);
      IDetailedSourceLocation location = context.getSourceLocation(member, region);
      return new TerminatingChainedMethodCall<>(location);
   }

   private IChainedMethodCall<String> thenGetString(String key) {
      Member member = getMember(key);
      Value value = member.getValue();
      if (!(value instanceof StringValue)) {
         throw new IllegalStateException("value for key " + key + " is not a json string");
      }
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(value);
      IDetailedSourceLocation location = context.getSourceLocation(value, region);
      location = location.getSubLocation(1, location.getLength() - 2);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<JsonObject> thenGetJsonObject(String key) {
      Member member = getMember(key);
      Value value = member.getValue();
      if (!(value instanceof com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue)) {
         throw new IllegalStateException("value for key " + key + " is not a json array");
      }
      return new JsonObjectChainedMethodCall(json.getJsonObject(key),
               ((com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue) value).getValue(), context);
   }

   private IChainedMethodCall<JsonArray> thenGetJsonArray(String key) {
      Member member = getMember(key);
      Value value = member.getValue();
      if (!(value instanceof ArrayValue)) {
         throw new IllegalStateException("value for key " + key + " is not a json array");
      }
      return new JsonArrayChainedMethodCall(json.getJsonArray(key), (ArrayValue) value, context);
   }

   private Member getMember(String key) {
      Preconditions.checkNotNull(key, "key cannot be null");
      for (Member member : xtextJson.getMembers()) {
         if (key.equals(member.getKey())) {
            return member;
         }
      }
      throw new IllegalStateException("key " + key + " not a JsonObject " + json);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextJson);
      return context.getSourceLocation(xtextJson, region);
   }

}
