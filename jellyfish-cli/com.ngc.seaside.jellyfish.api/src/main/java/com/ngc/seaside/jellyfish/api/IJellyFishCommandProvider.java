package com.ngc.seaside.jellyfish.api;

/**
 * The JellyFish command provider interface will provide JellyFish commands and allow access to those
 * commands via calls to this interface. The run method will parse the input parameters but will not validate the
 * contents. The individual command must validate their own input.
 */
public interface IJellyFishCommandProvider extends ICommandProvider<
      IJellyFishCommandOptions,
      IJellyFishCommand,
      IJellyFishCommandOptions> {

   /**
    * Runs a command with the given options.  Use {@link DefaultJellyFishCommandOptions#mergeWith
    * (IJellyFishCommandOptions, Collection) mergeWith} to easily manage options.
    *
    * @param command        the name of the command to run
    * @param commandOptions the options to run the command with
    */
   void run(String command, IJellyFishCommandOptions commandOptions);
}
