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

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Base class for command provider implementations.
 *
 * @param <S> the type of command options the commands will accept
 * @param <T> the type of command
 * @param <R> the result of executing a command (use {@code Void} if this does not apply for a particular
 *            implementation)
 */
public abstract class AbstractCommandProvider<S extends ICommandOptions, T extends ICommand<S>, R>
      implements ICommandProvider<S, T, R> {

   /**
    * All registered commands, mapped by name.
    */
   protected final Map<String, T> commands = new ConcurrentHashMap<>();

   /**
    * The log service.
    */
   protected ILogService logService;

   /**
    * The parameter service.
    */
   protected IParameterService parameterService;

   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<T> commandsRef = new DeferredDynamicReference<T>() {
      @Override
      protected void addPostActivate(T command) {
         commands.put(command.getName(), command);
      }

      @Override
      protected void removePostActivate(T command) {
         commands.remove(command.getName());
      }
   };

   @Override
   public T getCommand(String commandName) {
      return commands.get(commandName);
   }

   @Override
   public void addCommand(T command) {
      commandsRef.add(command);
   }

   @Override
   public void removeCommand(T command) {
      commandsRef.remove(command);
   }

   /**
    * Activates this provider.
    */
   public void activate() {
      commandsRef.markActivated();
   }

   /**
    * Deactivates this provider.
    */
   public void deactivate() {
   }

   /**
    * Sets the log service.
    */
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Removes the log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets the parameter service.
    */
   public void setParameterService(IParameterService ref) {
      this.parameterService = ref;
   }

   /**
    * Removes the parameter service
    */
   public void removeParameterService(IParameterService ref) {
      setParameterService(null);
   }

   /**
    * Verifies that all the command's required parameters are set in the given collection of parameters.  If a required
    * parameter is missing, a {@code CommandException} is generated.
    *
    * @param command    the command that will be executed
    * @param parameters the provided parameters
    * @throws CommandException if a required parameter is not set
    */
   protected void verifyRequiredParameters(T command, IParameterCollection parameters) {
      String missingParams = command.getUsage().getRequiredParameters()
            .stream()
            .filter(p -> !parameters.containsParameter(p.getName()))
            .map(IParameter::getName)
            .collect(Collectors.joining(", "));
      if (!missingParams.isEmpty()) {
         String msg = String.format("the command '%s' requires the following additional parameters: %s",
                                    command.getName(),
                                    missingParams);
         throw new CommandException(msg);
      }
   }
}
