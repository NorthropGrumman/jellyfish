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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.IGherkinUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.InternalNodeModelUtils;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.LineAndColumn;

import javax.inject.Inject;

public class XTextSourceLocatorService implements ISourceLocatorService {

   private ILocationInFileProvider provider;
   private final ChainedMethodCallContext context;

   @Inject
   public XTextSourceLocatorService(ILocationInFileProvider provider) {
      this.provider = provider;
      this.context = new ChainedMethodCallContext(provider);
   }

   @Override
   public ISourceLocation getLocation(Object element, boolean fullRegion) {
      if (element instanceof IUnwrappable) {
         return doGetXtextLocation(element, fullRegion);
      } else if (element instanceof IGherkinUnwrappable) {
         return doGetGherkinLocation(element);
      }
      throw new UnknownSourceLocationException("Unable to get source location for element " + element);
   }

   private ISourceLocation doGetXtextLocation(Object element, boolean fullRegion) {
      @SuppressWarnings("unchecked")
      IUnwrappable<? extends EObject> wrappable = (IUnwrappable<? extends EObject>) element;
      EObject object = wrappable.unwrap();
      URI uri = EcoreUtil2.getNormalizedURI(object);
      INode node = NodeModelUtils.findActualNodeFor(object);
      ITextRegion region;
      if (fullRegion) {
         region = provider.getFullTextRegion(object);
      } else {
         region = provider.getSignificantTextRegion(object);
      }
      LineAndColumn lineAndColumn = WrapperInternalNodeModelUtils.getLineAndColumn(node, region.getOffset());
      return new SourceLocation(uri, lineAndColumn.getLine(), lineAndColumn.getColumn(), region.getLength());
   }

   private ISourceLocation doGetGherkinLocation(Object element) {
      IGherkinUnwrappable<?> wrapped = (IGherkinUnwrappable<?>) element;
      // Length information is not available from Gherkin.
      return new SourceLocation(wrapped.getPath(), wrapped.getLineNumber(), wrapped.getColumn(), -1);
   }

   private static class WrapperInternalNodeModelUtils extends InternalNodeModelUtils {
      public static LineAndColumn getLineAndColumn(INode node, int offset) {
         return InternalNodeModelUtils.getLineAndColumn(node, offset);
      }
   }

   @Override
   public <T> IChainedMethodCall<T> with(T element) {
      Preconditions.checkNotNull(element, "element may not be null");
      return context.getChainedMethodCallForElement(element);
   }
}
