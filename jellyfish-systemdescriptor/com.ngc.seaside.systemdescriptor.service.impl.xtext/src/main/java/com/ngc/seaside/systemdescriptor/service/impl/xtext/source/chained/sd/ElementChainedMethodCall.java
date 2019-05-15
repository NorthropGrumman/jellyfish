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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.util.ITextRegion;

public abstract class ElementChainedMethodCall<T extends INamedChild<IPackage>, E extends Element>
         extends AbstractXtextChainedMethodCall<T, E> {

   protected ElementChainedMethodCall(T element, E xtextElement, ChainedMethodCallContext context) {
      super(element, xtextElement, context);
   }

   protected IChainedMethodCall<String> thenGetName() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.ELEMENT__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new StringChainedMethodCall(location, context);
   }

   protected IChainedMethodCall<IPackage> thenGetParent() {
      Package pkg = (Package) xtextElement.eContainer();
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(pkg,
               SystemDescriptorPackage.Literals.PACKAGE__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new PackageChainedMethodCall(element.getParent(), location, context);
   }

   protected IChainedMethodCall<IMetadata> thenGetMetadata() {
      return context.getChainedMethodCallForElement(getMetadata(), element);
   }

   protected abstract IMetadata getMetadata();

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement);
      return context.getSourceLocation(xtextElement, region);
   }

}
