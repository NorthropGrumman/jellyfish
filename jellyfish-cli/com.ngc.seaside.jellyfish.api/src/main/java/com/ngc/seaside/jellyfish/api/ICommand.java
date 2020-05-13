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
