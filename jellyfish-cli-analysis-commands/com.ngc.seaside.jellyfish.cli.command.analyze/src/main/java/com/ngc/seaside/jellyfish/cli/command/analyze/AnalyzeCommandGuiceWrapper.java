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
package com.ngc.seaside.jellyfish.cli.command.analyze;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;

/**
 * Wrapper for the analyze command.
 */
public class AnalyzeCommandGuiceWrapper implements IJellyFishCommand {

   private final AnalyzeCommand delegate = new AnalyzeCommand();

   /**
    * Creates a new wrapper.
    */
   @SuppressWarnings({"unchecked", "rawtypes"})
   @Inject
   public AnalyzeCommandGuiceWrapper(ILogService logService,
                                     IJellyFishCommandProvider jellyFishCommandProvider,
                                     ICommandProvider commandProvider) {
      delegate.setLogService(logService);
      delegate.setJellyFishCommandProvider(jellyFishCommandProvider);
      delegate.setCommandProvider(commandProvider);
      delegate.activate();
   }

   @Override
   public String getName() {
      return delegate.getName();
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public void run(IJellyFishCommandOptions options) {
      delegate.run(options);
   }
}
