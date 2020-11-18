/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.impl.provider;

import java.util.Arrays;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.common.base.Preconditions;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

/**
 * A default command provider that runs non Jellyfish commands.  These are commands that implement the {@code ICommand}
 * interface directly and do no require a valid SD project in order to be executed.
 */
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

   @Override
   public void run(String command, ICommandOptions commandOptions) {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(commandOptions, "commandOptions may not be null!");
      ICommand<ICommandOptions> c = getCommand(command);
      Preconditions.checkArgument(c != null, "no command named '%s' found!", command);
      verifyRequiredParameters(c, commandOptions.getParameters());
      c.run(commandOptions);
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
