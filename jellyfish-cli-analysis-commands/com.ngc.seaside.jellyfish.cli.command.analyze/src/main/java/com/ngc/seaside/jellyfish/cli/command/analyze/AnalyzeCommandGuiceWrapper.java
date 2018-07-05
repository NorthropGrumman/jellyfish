package com.ngc.seaside.jellyfish.cli.command.analyze;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

/**
 * Wrapper for the analyze command.
 */
public class AnalyzeCommandGuiceWrapper implements IJellyFishCommand {

   private final AnalyzeCommand delegate = new AnalyzeCommand();

   /**
    * Creates a new wrapper.
    */
   @SuppressWarnings({"unchecked"})
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
