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
package com.ngc.seaside.jellyfish.cli.command.analyzestyle;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

/**
 * The model for the analysis command and its finding types.
 */
public class AnalyzeStyleCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
               .addBinding()
               .to(AnalyzeStyleCommand.class);

      // Register the finding types for this analysis.
      Multibinder<ISystemDescriptorFindingType> typesBinder = Multibinder.newSetBinder(
               binder(),
               ISystemDescriptorFindingType.class);
      for (ISystemDescriptorFindingType type : StyleFindingTypes.values()) {
         typesBinder.addBinding().toInstance(type);
      }
   }
}
