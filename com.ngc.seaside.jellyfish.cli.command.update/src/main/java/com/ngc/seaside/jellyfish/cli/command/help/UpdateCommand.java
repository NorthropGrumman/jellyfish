package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public final class UpdateCommand implements IJellyFishCommand {

   private static final String COMMAND_NAME = "update";

   private ILogService logService;

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Override
   public String getName() {
      return COMMAND_NAME;
   }

   @Override
   public IUsage getUsage() {
      return COMMAND_USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      Preconditions.checkNotNull(commandOptions);

   }

   private static final IUsage COMMAND_USAGE = new DefaultUsage(
         "Prints this help",
         new DefaultParameter<>("verbose")
               .setDescription("Prints the help of all of the known commands")
               .setRequired(false),
         new DefaultParameter<>("command")
               .setDescription("Command to print help")
               .setRequired(false));

}
