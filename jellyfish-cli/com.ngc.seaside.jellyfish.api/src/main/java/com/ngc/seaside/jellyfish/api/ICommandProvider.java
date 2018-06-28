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
