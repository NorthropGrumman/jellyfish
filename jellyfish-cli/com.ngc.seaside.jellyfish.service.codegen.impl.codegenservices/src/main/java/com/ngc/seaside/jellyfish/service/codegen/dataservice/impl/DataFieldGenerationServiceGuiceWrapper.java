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
package com.ngc.seaside.jellyfish.service.codegen.dataservice.impl;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedProtoField;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

public class DataFieldGenerationServiceGuiceWrapper implements IDataFieldGenerationService {

   private final DataFieldGenerationService delegate = new DataFieldGenerationService();
   
   @Inject
   public DataFieldGenerationServiceGuiceWrapper(IPackageNamingService packageNameService) {
      delegate.setPackageNamingService(packageNameService);
   }
   
   @Override
   public IGeneratedJavaField getEventsField(IJellyFishCommandOptions options, IDataField field) {
      return delegate.getEventsField(options, field);
   }

   @Override
   public IGeneratedProtoField getMessagesField(IJellyFishCommandOptions options, IDataField field) {
      return delegate.getMessagesField(options, field);
   }

}
