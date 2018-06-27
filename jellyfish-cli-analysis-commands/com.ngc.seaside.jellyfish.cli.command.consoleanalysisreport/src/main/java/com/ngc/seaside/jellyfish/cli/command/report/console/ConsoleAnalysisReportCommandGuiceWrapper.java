package com.ngc.seaside.jellyfish.cli.command.report.console;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;

public class ConsoleAnalysisReportCommandGuiceWrapper implements ICommand<ICommandOptions> {

   private final ConsoleAnalysisReportCommand delegate = new ConsoleAnalysisReportCommand();

   @Inject
   public ConsoleAnalysisReportCommandGuiceWrapper(ILogService logService,
                                                   IAnalysisService analysisService) {
      delegate.setLogService(logService);
      delegate.setAnalysisService(analysisService);
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
   public void run(ICommandOptions options) {
      delegate.run(options);
   }
}
