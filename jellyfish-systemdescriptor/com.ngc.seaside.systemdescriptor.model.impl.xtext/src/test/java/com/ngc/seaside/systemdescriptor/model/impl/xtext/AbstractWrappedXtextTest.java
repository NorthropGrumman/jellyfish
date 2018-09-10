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
package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractWrappedXtextTest {

   @Mock
   private IWrapperResolver resolver;

   private static SystemDescriptorFactory factory;

   @BeforeClass
   public static void setupClass() {
      factory = SystemDescriptorFactory.eINSTANCE;
   }

   protected IWrapperResolver resolver() {
      return resolver;
   }

   public static SystemDescriptorFactory factory() {
      return factory;
   }

   public static IMetadata newMetadata(String key, String value) {
      return new WrappedMetadata()
            .setJson(Json.createObjectBuilder().add(key, value).build());
   }
}
