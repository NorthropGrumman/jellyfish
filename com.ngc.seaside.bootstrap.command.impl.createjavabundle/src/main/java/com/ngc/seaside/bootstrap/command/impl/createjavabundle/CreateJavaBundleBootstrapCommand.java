package com.ngc.seaside.bootstrap.command.impl.createjavabundle;

import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;

/**
 *
 */
public class CreateJavaBundleBootstrapCommand implements IBootstrapCommand {
   private static final String NAME = "create-java-bundle";

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("");
   }

   @Override
   public void run(IBootstrapCommandOptions commandOptions) {
      System.out.println("Running " + NAME + " command.");
   }
}
