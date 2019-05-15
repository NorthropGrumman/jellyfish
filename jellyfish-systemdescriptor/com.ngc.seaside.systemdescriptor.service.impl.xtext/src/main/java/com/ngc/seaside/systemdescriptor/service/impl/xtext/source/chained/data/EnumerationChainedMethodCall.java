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

import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.ElementChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.util.ITextRegion;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class EnumerationChainedMethodCall extends ElementChainedMethodCall<IEnumeration, Enumeration> {

   /**
    * @param enumeration enumeration
    * @param xtextEnumeration xtext enumeration
    * @param context context
    */
   public EnumerationChainedMethodCall(IEnumeration enumeration, Enumeration xtextEnumeration,
                                       ChainedMethodCallContext context) {
      super(enumeration, xtextEnumeration, context);

      try {
         register(IEnumeration.class.getMethod("getName"), this::thenGetName);
         register(IEnumeration.class.getMethod("getParent"), this::thenGetParent);
         register(IEnumeration.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IEnumeration.class.getMethod("getValues"), this::thenGetValues);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<Collection<String>> thenGetValues() {
      ITextRegion firstRegion = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.ENUMERATION__VALUES, 0);
      ITextRegion lastRegion = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.ENUMERATION__VALUES, Math.max(0, xtextElement.getValues().size() - 1));

      ITextRegion region = firstRegion.merge(lastRegion);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);

      List<Entry<String, IChainedMethodCall<String>>> list = new ArrayList<>(element.getValues().size());
      int index = 0;
      for (String value : element.getValues()) {
         ITextRegion elementRegion = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.ENUMERATION__VALUES, index);
         IDetailedSourceLocation elementLocation = context.getSourceLocation(xtextElement, elementRegion);
         StringChainedMethodCall elementCall = new StringChainedMethodCall(elementLocation, context);
         Entry<String, IChainedMethodCall<String>> entry = new SimpleImmutableEntry<>(value, elementCall);
         list.add(entry);
         index++;
      }

      return new CollectionChainedMethodCall<>(location, element.getValues(), list, context);
   }

   @Override
   protected IMetadata getMetadata() {
      return element.getMetadata();
   }
}
