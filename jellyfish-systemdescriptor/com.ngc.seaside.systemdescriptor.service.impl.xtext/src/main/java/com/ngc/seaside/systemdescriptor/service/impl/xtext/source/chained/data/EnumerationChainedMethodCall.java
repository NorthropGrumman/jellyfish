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
