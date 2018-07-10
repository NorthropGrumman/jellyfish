package com.ngc.seaside.jellyfish.cli.command.report.html;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

/**
 * The Guice wrapper for the HTML command.
 */
public class HtmlAnalysisReportCommandGuiceWrapper implements ICommand<ICommandOptions> {

   private final HtmlAnalysisReportCommand delegate = new HtmlAnalysisReportCommand();

   /**
    * Creates a new command wrapper.
    */
   @Inject
   public HtmlAnalysisReportCommandGuiceWrapper(ILogService logService,
                                                ITemplateService templateService,
                                                IAnalysisService analysisService,
                                                IReportingOutputService reportingOutputService) {
      delegate.setLogService(logService);
      delegate.setAnalysisService(analysisService);
      delegate.setReportingOutputService(reportingOutputService);
      delegate.setTemplateService(templateService);
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
