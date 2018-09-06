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
package com.ngc.seaside.jellyfish.service.execution.api;

import com.google.inject.Module;

import java.util.Collection;
import java.util.Map;

/**
 * Allows for the embedding and execution of Jellyfish.  Clients can use either {@code run} method to execute Jellyfish
 * from within their application.  Clients pass in the {@code Module}s to use to create Jellyfish and execute.  In most
 * cases clients will pass in {@code com.ngc.seaside.jellyfish.DefaultJellyfishModule} directly to extend the default
 * module.
 */
public interface IJellyfishService {

   /**
    * Executes Jellyfish using the given arguments.  The arguments are expected to in the form {@code
    * argumentName=value} or {@code argumentName}.
    *
    * @param command   the Jellyfish command to run
    * @param arguments the arguments to run the command with
    * @param modules   the modules to use to run Jellyfish with (consider using a subclass of {@code
    *                  com.ngc.seaside.jellyfish.DefaultJellyfishModule})
    * @return the results of executing Jellyfish
    * @throws JellyfishExecutionException if the execution of Jellyfish failed.  This can indicate an unknown command
    *                                     was reference or some misconfiguration.  This typically does not indicate a
    *                                     parsing error.
    */
   IJellyfishExecution run(String command, Collection<String> arguments, Collection<Module> modules)
         throws JellyfishExecutionException;

   /**
    * Executes Jellyfish using the given arguments.  The keys of the map are used as the argument names and the value
    * are used as the values of the argument.
    *
    * @param command   the Jellyfish command to run
    * @param arguments the arguments to run the command with
    * @param modules   the modules to use to run Jellyfish with (consider using a subclass of {@code
    *                  com.ngc.seaside.jellyfish.DefaultJellyfishModule})
    * @return the results of executing Jellyfish
    * @throws JellyfishExecutionException if the execution of Jellyfish failed.  This can indicate an unknown command
    *                                     was reference or some misconfiguration.  This typically does not indicate a
    *                                     parsing error.
    */
   IJellyfishExecution run(String command, Map<String, String> arguments, Collection<Module> modules)
         throws JellyfishExecutionException;
}
