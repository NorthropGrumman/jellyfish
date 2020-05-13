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
