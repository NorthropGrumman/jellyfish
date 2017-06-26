package com.ngc.seaside.jellyfish.cli.command.validate;

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
 * This class provides the implementation of the validate command. It currently has
 * no functionality.
 * 
 * @author blake.perkins@ngc.com
 */
public class ValidateCommand implements IJellyFishCommand {
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
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
      // TODO Add usage once we determine how this command will be used.
      return new DefaultUsage("Fix me");
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      logService.trace(getClass(), "Running command %s", NAME);
      if (commandOptions == null || commandOptions.getSystemDescriptor() == null) {
         logService.error(getClass(), "System Descriptor is null.  Verify project path and syntax.");
         throw new IllegalArgumentException("System Descriptor is null.  Verify project path and syntax.");
      } else {
         logService.info(getClass(), "System Descriptor validated!");
      }
   }

   @Override
   public String toString() {
      return getName();
   }
}
