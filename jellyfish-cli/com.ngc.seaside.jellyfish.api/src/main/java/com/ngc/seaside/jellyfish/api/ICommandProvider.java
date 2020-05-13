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
package com.ngc.seaside.jellyfish.api;

/**
 * The implementations of this class provide a convenient way to run and provide the usage for a
 * collection of commands. The type of command's that a provider can run will really depend on the
 * type of options that the commands require. The only required options for running a command are
 * the input parameters which should be described via the {@link #getUsage()} method.
 *
 * @see ICommand
 */
public interface ICommandProvider<S extends ICommandOptions, T extends ICommand<S>, R> {

   /**
    * The usage's description should provide a detailed set of instructions on how to use this
    * provider and all of the available commands.
    *
    * @return the provider's and its commands usage.
    */
   IUsage getUsage();

   /**
    * Get the command.
    *
    * @param commandName the command name.
    * @return the command
    */
   T getCommand(String commandName);

   /**
    * Add a command to this provider.
    *
    * @param command the command.
    */
   void addCommand(T command);

   /**
    * Remove a command from this provider.
    *
    * @param command the command.
    */
   void removeCommand(T command);

   /**
    * Run the command by name. The options will vary depending on what type of commands this
    * provider can run.
    *
    * @param arguments the arguments used to run a command
    * @return the results of the command
    */
   R run(String[] arguments);

   /**
    * Runs a command with the given options.
    *
    * @param command        the name of the command to run
    * @param commandOptions the options to run the command with
    */
   void run(String command, S commandOptions);
}
