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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.WrappedChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.EnumChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.ITextRegion;

public class DataReferenceFieldChainedMethodCall extends AbstractChainedMethodCall<IDataReferenceField> {

   private final IDataReferenceField field;
   private final EObject xtextField;

   /**
    * @param field field
    * @param xtextField xtext field
    * @param context context
    */
   public DataReferenceFieldChainedMethodCall(IDataReferenceField field, EObject xtextField,
                                              ChainedMethodCallContext context) {
      super(field, context);

      this.field = field;
      this.xtextField = xtextField;
      try {
         register(IReferenceField.class.getMethod("getName"), this::thenGetName);
         register(IReferenceField.class.getMethod("getParent"), this::thenGetParent);
         register(IReferenceField.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IDataReferenceField.class.getMethod("getType"), this::thenGetType);
         register(IDataReferenceField.class.getMethod("getCardinality"), this::thenGetCardinality);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetName() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField,
               SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextField, region);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<IData> thenGetType() {
      IChainedMethodCall<IData> call = context.getChainedMethodCallForElement(field.getType());
      return new WrappedChainedMethodCall<IData>(call) {
         @Override
         public ISourceLocation getLocation() {
            ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField,
                     SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__TYPE, 0);
            IDetailedSourceLocation location = context.getSourceLocation(xtextField, region);
            return location;
         }
      };
   }

   private IChainedMethodCall<FieldCardinality> thenGetCardinality() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField,
               SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__CARDINALITY, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextField, region);
      return new EnumChainedMethodCall<>(location, context);
   }

   private IChainedMethodCall<IModel> thenGetParent() {
      return null;
   }

   private IChainedMethodCall<IMetadata> thenGetMetadata() {
      return null;
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField);
      return context.getSourceLocation(xtextField, region);
   }

}
