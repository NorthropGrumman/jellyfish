package com.ngc.seaside.jellyfish.cli.command.validate;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.utilities.parsing.ParsingResultLogging;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * This class provides the implementation of the validate command.
 */
@Component(service = IJellyFishCommand.class)
public class ValidateCommand implements IJellyFishCommand {

   private static final String NAME = "validate";
   private static final IUsage
         COMMAND_USAGE =
         new DefaultUsage("Validates a System Descriptor project for syntax warnings and errors");

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Reference
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

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
      IParsingResult result = commandOptions.getParsingResult();
      if (result.isSuccessful()) {
         ParsingResultLogging.logWarnings(result).forEach(l -> logService.warn(getClass(), "%s", l));
         logService.info(ValidateCommand.class, "System Descriptor project is valid.");
      } else {
         // The formatting of the logging line with "%s" avoids issues if the line in the model file contains a
         // "%s" symbol.
         ParsingResultLogging.logErrors(result).forEach(l -> logService.error(getClass(), "%s", l));
         ParsingResultLogging.logWarnings(result).forEach(l -> logService.warn(getClass(), "%s", l));
         throw new CommandException("System Descriptor failed validation!");
      }
   }

   @Override
   public String toString() {
      return getName();
   }
}
