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
