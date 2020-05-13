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
