package com.ngc.seaside.jellyfish.cli.command.analyze;

import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;

/**
 * The top level analyze command.  This command uses the {@code analysis} and {@code reports} parameters to call
 * one or more analysis and reporting commands.  This command does little work itself.  It mostly delegates to other
 * commands.
 */
public class AnalyzeCommand extends AbstractJellyfishCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze";

   private IJellyFishCommandProvider jellyFishCommandProvider;

   private ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> commandProvider;

   /**
    * Creates a new command.
    */
   public AnalyzeCommand() {
      super(NAME);
   }

   @Override
   public void activate() {
      logService.debug(AnalyzeCommand.class, "Activated.");
   }

   @Override
   public void deactivate() {
      logService.debug(AnalyzeCommand.class, "Deactivated.");
   }

   /**
    * Sets the Jellyfish command provider.
    */
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyFishCommandProvider = ref;
   }

   /**
    * Removes the Jellyfish command provider.
    */
   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }

   /**
    * Sets the basic command provider.
    */
   public void setCommandProvider(
         ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> ref) {
      this.commandProvider = ref;
   }

   /**
    * Removes the basic command provider.
    */
   public void removeCommandProvider(
         ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> ref) {
      setCommandProvider(null);
   }

   @Override
   protected IUsage createUsage() {
      // TODO TH: This command should use 2 required parameters:
      // 1) analysis: this is a comma separated list of analysis commands that should be executed.
      // 2) reports: this is a comma separated list of report commands that should be executed after performing analysis
      return new DefaultUsage("Run various types of analysis and generates reports.");
   }

   @Override
   protected void doRun() {
      // TODO TH: This command should use the values of the analysis and reports parameters to figure out what to do.
      // These parameters should be required.
      // The command should first run the analysis and then run the reports.  Note the impl should use
      // jellyFishCommandProvider.getCommand(command) != null and/or commandProvider.getCommand(command) != null
      // to determine which provider the command is registered with.

      // This is for demos only, hard coding the analysis and the reports for now.  Remove this once the above is
      // implemented.
      jellyFishCommandProvider.run("analyze-inputs-outputs", getOptions());
      commandProvider.run("html-report", getOptions());
   }
}
