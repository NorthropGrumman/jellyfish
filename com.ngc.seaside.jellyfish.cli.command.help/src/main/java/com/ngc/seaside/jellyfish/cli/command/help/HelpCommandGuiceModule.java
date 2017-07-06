package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import java.util.Set;

public class HelpCommandModule extends AbstractModule implements IJellyFishCommand {

   private final HelpCommand delegate = new HelpCommand();
   
   
   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(HelpCommand.class).asEagerSingleton();

      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(HelpCommand.class).asEagerSingleton();

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
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }
   
   @Inject
   public void addCommands(Set<IJellyFishCommand> commands) {
      commands.forEach(delegate::addCommand);
   }
   
}
