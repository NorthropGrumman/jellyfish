package com.ngc.seaside.jellyfish.cli.command.help;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

public class HelpCommand implements IJellyFishCommand
{
   public final static String COMMAND_NAME = "help";

   private ILogService logService;

   @Override
   public String getName()
   {
      return COMMAND_NAME;
   }

   @Override
   public IUsage getUsage()
   {
      // TODO
      return new DefaultUsage("");
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions)
   {
      // TODO Auto-generated method stub
   }

   @Activate
   public void activate()
   {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate()
   {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   @Inject
   public void setLogService(ILogService ref)
   {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref)
   {
      setLogService(null);
   }

}
