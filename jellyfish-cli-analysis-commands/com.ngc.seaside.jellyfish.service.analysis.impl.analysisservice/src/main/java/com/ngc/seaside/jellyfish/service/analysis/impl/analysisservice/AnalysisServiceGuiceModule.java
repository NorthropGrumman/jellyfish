package com.ngc.seaside.jellyfish.service.analysis.impl.analysisservice;

import com.google.inject.AbstractModule;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;

public class AnalysisServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IAnalysisService.class).to(AnalysisService.class).asEagerSingleton();
   }

}
