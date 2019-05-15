/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
