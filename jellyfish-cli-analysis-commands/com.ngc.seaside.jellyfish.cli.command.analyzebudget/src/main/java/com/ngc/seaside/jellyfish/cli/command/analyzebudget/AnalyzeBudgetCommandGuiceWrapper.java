package com.ngc.seaside.jellyfish.cli.command.analyzebudget;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.SdBudgetAdapter;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import javax.inject.Inject;

public class AnalyzeBudgetCommandGuiceWrapper implements IJellyFishCommand {

   private final AnalyzeBudgetCommand delegate = new AnalyzeBudgetCommand();

   /**
    * Creates a new wrapper.
    */
   @Inject
   public AnalyzeBudgetCommandGuiceWrapper(ILogService logService,
                                           IAnalysisService analysisService,
                                           ISourceLocatorService sourceLocatorService,
                                           SdBudgetAdapter adapter) {
      delegate.setLogService(logService);
      delegate.setAnalysisService(analysisService);
      delegate.setSourceLocatorService(sourceLocatorService);
      delegate.setBudgetAdapter(adapter);
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
