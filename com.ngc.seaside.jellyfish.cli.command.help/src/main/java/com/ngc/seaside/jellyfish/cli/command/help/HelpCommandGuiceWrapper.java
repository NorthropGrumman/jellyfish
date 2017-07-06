package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import java.util.Set;

@Singleton
public class HelpCommandGuiceWrapper implements IJellyFishCommand {

   private final HelpCommand delegate = new HelpCommand();

   @Inject
   public HelpCommandGuiceWrapper(ILogService logService) {
      delegate.setLogService(logService);
   }
   
   @Inject
   public void addCommands(Set<IJellyFishCommand> commands) {
      commands.forEach(delegate::addCommand);
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
   
}
