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
package com.ngc.seaside.jellyfish;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishService;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The main entry point of Jellyfish.  {@link #main(String[])} is used when running Jellyfish from the command line.  If
 * embedding Jellyfish in another application use {@link #getService()}.
 */
public class Jellyfish {

   private static final IJellyfishService SERVICE_INSTANCE = new JellyfishService();

   /**
    * Runs Jellyfish from the command line.
    *
    * @param args the jellyfish command line arguments
    */
   public static void main(String[] args) {
      Preconditions.checkNotNull(args, "args may not be null!");
      Preconditions.checkArgument(args.length > 0,
                                  "please run Jellyfish with at least one argument, try 'jellyfish help' for help!");
      String command = args[0];
      Collection<String> remainingArgs = new ArrayList<>();
      for (int i = 1; i < args.length; i++) {
         remainingArgs.add(args[i]);
      }
      // Run the service with the default set of modules.
      IJellyfishExecution result = getService().run(command,
                                                    remainingArgs,
                                                    Collections.singleton(new DefaultJellyfishModule()));
      System.out.printf("-- SUCCESS (%s) --%n", DurationFormatUtils.formatDurationHMS(result.getExecutionDuration()));
   }

   /**
    * Gets an instance of the {@link IJellyfishService} which can be used to programmatically run Jellyfish from within
    * another application.
    *
    * @return a instance of {@link IJellyfishService}
    */
   public static IJellyfishService getService() {
      return SERVICE_INSTANCE;
   }
}
