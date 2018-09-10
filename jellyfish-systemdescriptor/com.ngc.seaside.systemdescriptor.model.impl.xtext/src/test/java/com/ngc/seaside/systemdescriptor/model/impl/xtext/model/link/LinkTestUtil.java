/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;

public class LinkTestUtil {

   private LinkTestUtil() {
   }

   public static class LinkTestSetup {

      public final LinkDeclaration declaration;
      public final IWrapperResolver resolver;

      public LinkTestSetup(LinkDeclaration declaration,
                           IWrapperResolver resolver) {
         this.declaration = declaration;
         this.resolver = resolver;
      }
   }
}
