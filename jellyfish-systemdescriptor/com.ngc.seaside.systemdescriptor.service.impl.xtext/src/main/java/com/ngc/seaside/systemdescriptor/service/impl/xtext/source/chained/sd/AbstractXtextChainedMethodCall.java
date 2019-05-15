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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.ITextRegion;

public abstract class AbstractXtextChainedMethodCall<T, E extends EObject> extends AbstractChainedMethodCall<T> {

   protected final T element;
   protected final E xtextElement;
   
   protected AbstractXtextChainedMethodCall(T element, E xtextElement, ChainedMethodCallContext context) {
      super(element, context);
      this.element = Preconditions.checkNotNull(element, "element cannot be null");
      this.xtextElement = Preconditions.checkNotNull(xtextElement, "xtext element cannot be null");
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement);
      return context.getSourceLocation(xtextElement, region);
   }

}
