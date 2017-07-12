package com.ngc.seaside.jellyfish.cli.command.validate;

import com.ngc.blocs.service.log.api.ILogService;
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

/**
 * This class provides the implementation of the validate command.
 */
@Component(service = IJellyFishCommand.class)
public class ValidateCommand implements IJellyFishCommand {
   private static final String NAME = "validate";
   private static final IUsage COMMAND_USAGE = new DefaultUsage("Validates the System Descriptor. Requires a system descriptor project within src/main/sd");

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
      return COMMAND_USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      // A System Descriptor is considered valid if it isn't null
      logService.trace(getClass(), "Running command %s", NAME);
      if (commandOptions == null || commandOptions.getSystemDescriptor() == null) {
         throw new IllegalArgumentException("System Descriptor is invalid.  Verify the correct project path and syntax.");
      } else {
         logService.info(getClass(), "System Descriptor is valid");
      }
   }

   @Override
   public String toString() {
      return getName();
   }
}
