package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;

public class ScenarioServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IScenarioService.class).to(ScenarioService.class).in(Singleton.class);
   }
}
