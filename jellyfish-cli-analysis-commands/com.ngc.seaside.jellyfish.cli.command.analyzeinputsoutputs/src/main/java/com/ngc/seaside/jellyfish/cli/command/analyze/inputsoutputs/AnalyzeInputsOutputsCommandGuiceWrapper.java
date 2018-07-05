package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

/**
 * The wrapper for the inputs and outputs command.
 */
public class AnalyzeInputsOutputsCommandGuiceWrapper implements IJellyFishCommand {

   private final AnalyzeInputsOutputsCommand delegate = new AnalyzeInputsOutputsCommand();

   /**
    * Creates a new wrapper.
    */
   @Inject
   public AnalyzeInputsOutputsCommandGuiceWrapper(ILogService logService,
                                                  IAnalysisService analysisService,
                                                  ISourceLocatorService sourceLocatorService) {
      delegate.setLogService(logService);
      delegate.setAnalysisService(analysisService);
      delegate.setSourceLocatorService(sourceLocatorService);
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
