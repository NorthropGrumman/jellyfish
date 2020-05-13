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
