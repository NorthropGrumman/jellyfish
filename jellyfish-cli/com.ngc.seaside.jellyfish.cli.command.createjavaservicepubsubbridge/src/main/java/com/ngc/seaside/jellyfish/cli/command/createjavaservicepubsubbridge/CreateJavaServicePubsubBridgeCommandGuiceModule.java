package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto.IPubSubBridgeDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto.PubSubBridgeDtoFactory;

public class CreateJavaServicePubsubBridgeCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(CreateJavaServicePubsubBridgeCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
          .addBinding()
          .to(CreateJavaServicePubsubBridgeCommandGuiceWrapper.class);
      bind(IPubSubBridgeDtoFactory.class)
            .to(PubSubBridgeDtoFactory.class);
   }
}
