package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ServiceDtoFactory;

public class CreateJavaServiceCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(CreateJavaServiceCommandGuiceWrapper.class);
      bind(IServiceDtoFactory.class)
            .to(ServiceDtoFactory.class).asEagerSingleton();
   }
}
