package com.ngc.seaside.bootstrap.command.impl.createjavabundle;

import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;

/**
 * @author justan.provence@ngc.com
 */
public class CreateJavaBundle implements IBootstrapCommand {

   private IBootstrapCommandOptions commandOptions;

   @Override
   public String getName() {
      return "create-java-bundle";
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage(
            "Test class",
            new DefaultParameter("outputDirectory").setRequired(true),
            new DefaultParameter("anotherKey").setRequired(false));
   }

   @Override
   public void run(IBootstrapCommandOptions commandOptions) {
      this.commandOptions = commandOptions;
   }

   public IBootstrapCommandOptions getCommandOptions() {
      return commandOptions;
   }
}
