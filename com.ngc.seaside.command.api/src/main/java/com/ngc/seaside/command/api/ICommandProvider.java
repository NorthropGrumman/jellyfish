package com.ngc.seaside.command.api;

/**
 * The implementations of this class provide a convenient way to run and provide the usage for a
 * collection of commands. The type of command's that a provider can run will really depend on the
 * type of options that the commands require. The only required options for running a command are
 * the input parameters which should be described via the {@link #getUsage()} method.
 *
 * @see ICommand
 */
public interface ICommandProvider<T extends ICommand>  {

   /**
    * The usage's description should provide a detailed set of instructions on how to use this
    * provider and all of the available commands.
    *
    * @return the provider's and its commands usage.
    */
   IUsage getUsage();

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
    * @param arguments   the arguments used to run a command.
    */
   void run(String[] arguments);

}
