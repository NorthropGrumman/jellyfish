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
