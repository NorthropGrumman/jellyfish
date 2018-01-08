package com.ngc.seaside.command.api;

/**
 * The command interface provides a way to describe the usage of a command as well as run that command given
 * it's command options as input. This interface is very similar to the intent of the java.lang.Runnable interface.
 * This class is intended to be run by a {@link ICommandProvider} implementation.
 *
 * @author justan.provence@ngc.com
 */
public interface ICommand<T extends ICommandOptions> {

   /**
    * This name should return the name that is invoked from the command line.
    * Such as create-project or import-bundle.
    *
    * @return the name.
    */
   String getName();

   /**
    * Get the way this command is used.
    *
    * @return the usage.
    */
   IUsage getUsage();

   /**
    * Run the command with the given options.
    *
    * @param commandOptions the data used to run this command. This will consist of parameters that
    *                       the user has set as well as any meta-data that might be necessary to
    *                       run it.
    */
   void run(T commandOptions);

}
