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

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.WrappedChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.EnumChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.AbstractXtextChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.util.ITextRegion;

public class DataFieldChainedMethodCall extends AbstractXtextChainedMethodCall<IDataField, DataFieldDeclaration> {

   /**
    * @param field field
    * @param xtextField xtext field
    * @param context context
    */
   public DataFieldChainedMethodCall(IDataField field, DataFieldDeclaration xtextField,
                                     ChainedMethodCallContext context) {
      super(field, xtextField, context);

      try {
         register(IDataField.class.getMethod("getName"), this::thenGetName);
         register(IDataField.class.getMethod("getParent"), this::thenGetParent);
         register(IDataField.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IDataField.class.getMethod("getCardinality"), this::thenGetCardinality);
         register(IDataField.class.getMethod("getReferencedDataType"), this::thenGetReferencedDataType);
         register(IDataField.class.getMethod("getReferencedEnumeration"), this::thenGetReferencedEnumeration);
         register(IDataField.class.getMethod("getType"), this::thenGetType);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetName() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<IData> thenGetParent() {
      return context.getChainedMethodCallForElement(element.getParent(), context);
   }

   private IChainedMethodCall<IMetadata> thenGetMetadata() {
      return context.getChainedMethodCallForElement(element.getMetadata(), element);
   }

   private IChainedMethodCall<FieldCardinality> thenGetCardinality() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__CARDINALITY, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new EnumChainedMethodCall<>(location, context);
   }

   private IChainedMethodCall<IData> thenGetReferencedDataType() {
      return new WrappedChainedMethodCall<IData>(
               context.getChainedMethodCallForElement(element.getReferencedDataType(), context)) {
         @Override
         public ISourceLocation getLocation() {
            ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                     SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL, 0);
            IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
            return location;
         }
      };
   }

   private IChainedMethodCall<IEnumeration> thenGetReferencedEnumeration() {
      return new WrappedChainedMethodCall<IEnumeration>(
               context.getChainedMethodCallForElement(element.getReferencedEnumeration(), context)) {
         @Override
         public ISourceLocation getLocation() {
            ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                     SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL, 0);
            IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
            return location;
         }
      };
   }

   private IChainedMethodCall<DataTypes> thenGetType() {
      ITextRegion region;
      if (xtextElement instanceof ReferencedDataModelFieldDeclaration) {
         region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL, 0);
      } else {
         region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION__TYPE, 0);
      }
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new EnumChainedMethodCall<>(location, context);
   }
}
