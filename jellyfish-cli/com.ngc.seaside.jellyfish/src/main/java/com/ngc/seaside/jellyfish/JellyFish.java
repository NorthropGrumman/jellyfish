package com.ngc.seaside.jellyfish;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The main entry point of Jellyfish.  {@link #main(String[])} is used when running Jellyfish from the command line.  If
 * embedding Jellyfish in another application use {@link #getService()}.
 */
public class JellyFish {

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
      getService().run(command, remainingArgs, Collections.singleton(new DefaultJellyfishModule()));
      System.out.println("-- SUCCESS --");
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
