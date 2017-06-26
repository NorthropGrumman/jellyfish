package com.ngc.seaside.jellyfish.command.impl.validatecommand;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author blake.perkins@ngc.com
 */
public class ValidateJellyFishCommand implements IJellyFishCommand {
   private final static String NAME = "validate";

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, 
            policy = ReferencePolicy.STATIC, 
            unbind = "removeLogService")
   @Inject
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("Fix me");
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      logService.trace(getClass(), "Running command %s", NAME);
      logService.info(getClass(), "Validate command currently does nothing.");
   }

   @Override
   public String toString() {
      return getName();
   }
}
