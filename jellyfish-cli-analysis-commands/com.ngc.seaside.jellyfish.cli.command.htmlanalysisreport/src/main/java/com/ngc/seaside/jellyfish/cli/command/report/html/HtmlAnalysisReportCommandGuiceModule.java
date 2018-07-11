package com.ngc.seaside.jellyfish.cli.command.report.html;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.ICommand;

/**
 * The Guice module for the HTML report command.
 */
public class HtmlAnalysisReportCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), ICommand.class)
            .addBinding()
            .to(HtmlAnalysisReportCommandGuiceWrapper.class);
   }
}
