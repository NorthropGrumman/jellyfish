/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
