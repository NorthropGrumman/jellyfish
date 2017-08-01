package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ITemplateDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceTemplateDaoFactory;

public class CreateJavaServiceBaseCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(CreateJavaServiceBaseCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
            .addBinding()
            .to(CreateJavaServiceBaseCommandGuiceWrapper.class);
      bind(ITemplateDtoFactory.class)
            .annotatedWith(Names.named("java-service-base"))
            .to(BaseServiceTemplateDaoFactory.class);
   }
}
