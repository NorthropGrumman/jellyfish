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
