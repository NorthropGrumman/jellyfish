package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;

public class FeatureServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IFeatureService.class).to(FeatureServiceGuiceWrapper.class)
                                      .in(Singleton.class);
   }

}
