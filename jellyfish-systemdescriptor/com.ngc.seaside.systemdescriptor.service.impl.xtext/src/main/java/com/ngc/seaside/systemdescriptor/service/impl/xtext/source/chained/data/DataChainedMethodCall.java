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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.NamedChildCollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.ElementChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.util.ITextRegion;

import java.util.Optional;

public class DataChainedMethodCall extends ElementChainedMethodCall<IData, Data> {

   /**
    * @param data data
    * @param xtextData xtext data
    * @param context context
    */
   public DataChainedMethodCall(IData data, Data xtextData, ChainedMethodCallContext context) {
      super(data, xtextData, context);
      try {
         register(IData.class.getMethod("getName"), this::thenGetName);
         register(IData.class.getMethod("getParent"), this::thenGetParent);
         register(IData.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IData.class.getMethod("getFields"), this::thenGetFields);
         register(IData.class.getMethod("getExtendedDataType"), this::thenGetExtendedDataType);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<INamedChildCollection<IData, IDataField>> thenGetFields() {
      return new NamedChildCollectionChainedMethodCall<>(null, element.getFields(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<Optional<IData>> thenGetExtendedDataType() {
      IData superType = element.getExtendedDataType().orElseThrow(() -> new UnknownSourceLocationException(
               "Data type " + element.getFullyQualifiedName() + " does not extend any other type"));
      IChainedMethodCall<IData> methodCall = context.getChainedMethodCallForElement(superType);
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.DATA__EXTENDED_DATA_TYPE, 0);
      ISourceLocation location = context.getSourceLocation(xtextElement, region);
      return new OptionalChainedMethodCall<>(methodCall, location, context);
   }

   @Override
   protected IMetadata getMetadata() {
      return element.getMetadata();
   }
}
