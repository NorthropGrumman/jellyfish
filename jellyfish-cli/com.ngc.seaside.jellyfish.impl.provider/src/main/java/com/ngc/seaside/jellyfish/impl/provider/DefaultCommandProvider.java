package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Arrays;
import java.util.Collections;

@Component(service = ICommandProvider.class)
public class DefaultCommandProvider extends AbstractCommandProvider<
      ICommandOptions,
      ICommand<ICommandOptions>,
      ICommandOptions> {

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("Default commands - Run various command that don't require a System Descriptor model",
                              Collections.emptyList());
   }

   @Override
   public ICommandOptions run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "arguments must not be null.");
      Preconditions.checkArgument(arguments.length > 0,
                                  "must supply at least a command name!");

      String commandName = arguments[0];
      IParameterCollection parameters = parameterService.parseParameters(Arrays.asList(arguments)
                                                                               .subList(1, arguments.length));
      return runCommand(commandName, parameters);
   }


   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   @Override
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeParameterService")
   @Override
   public void setParameterService(IParameterService ref) {
      super.setParameterService(ref);
   }

   private ICommandOptions runCommand(String commandName, IParameterCollection parameters) {
      ICommand<ICommandOptions> command = getCommand(commandName);
      Preconditions.checkArgument(command != null, "no command named '%s' found!", commandName);
      verifyRequiredParameters(command, parameters);

      // Use the default Jellyfish command options for convenience.
      DefaultJellyFishCommandOptions options = new DefaultJellyFishCommandOptions();
      options.setParameters(parameters);

      // Run the command.
      command.run(options);
      return options;
   }
}
