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
