package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;

public class RequirementsServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IRequirementsService.class).to(RequirementsServiceGuiceWrapper.class)
                                      .in(Singleton.class);
   }

}
