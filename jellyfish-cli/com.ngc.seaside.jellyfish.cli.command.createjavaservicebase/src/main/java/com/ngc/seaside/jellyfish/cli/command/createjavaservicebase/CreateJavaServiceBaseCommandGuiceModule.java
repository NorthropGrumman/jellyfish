package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;

public class CreateJavaServiceBaseCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(CreateJavaServiceBaseCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
            .addBinding()
            .to(CreateJavaServiceBaseCommandGuiceWrapper.class);
      bind(IBaseServiceDtoFactory.class)
            .to(BaseServiceDtoFactory.class);
   }
}
