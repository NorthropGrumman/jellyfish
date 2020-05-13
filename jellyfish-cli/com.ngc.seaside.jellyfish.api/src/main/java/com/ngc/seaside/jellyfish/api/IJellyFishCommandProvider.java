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
