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

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

public class PackageChainedMethodCall extends AbstractChainedMethodCall<IPackage> {

   private final IDetailedSourceLocation location;

   /**
    * @param pkg package
    * @param location location of package declaration
    * @param context context
    */
   public PackageChainedMethodCall(IPackage pkg, IDetailedSourceLocation location, ChainedMethodCallContext context) {
      super(pkg, context);

      this.location = location;
      try {
         register(IPackage.class.getMethod("getName"), this::thenGetName);
         register(IPackage.class.getMethod("getParent"), this::thenGetParent);
         register(IPackage.class.getMethod("getData"), this::thenGetData);
         register(IPackage.class.getMethod("getModels"), this::thenGetModels);
         register(IPackage.class.getMethod("getEnumerations"), this::thenGetEnumerations);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   public IChainedMethodCall<String> thenGetName() {
      return new StringChainedMethodCall(location, context);
   }

   public IChainedMethodCall<ISystemDescriptor> thenGetParent() {
      throw new UnsupportedOperationException("Not implemented");
   }

   public IChainedMethodCall<INamedChildCollection<?, ?>> thenGetData() {
      throw new UnsupportedOperationException("Not implemented");
   }

   public IChainedMethodCall<INamedChildCollection<?, ?>> thenGetModels() {
      throw new UnsupportedOperationException("Not implemented");
   }

   public IChainedMethodCall<INamedChildCollection<?, ?>> thenGetEnumerations() {
      throw new UnsupportedOperationException("Not implemented");
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      return location;
   }

}
