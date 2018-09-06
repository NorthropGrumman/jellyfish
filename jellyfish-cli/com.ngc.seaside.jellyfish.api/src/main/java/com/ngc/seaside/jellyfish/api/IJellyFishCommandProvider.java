/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
