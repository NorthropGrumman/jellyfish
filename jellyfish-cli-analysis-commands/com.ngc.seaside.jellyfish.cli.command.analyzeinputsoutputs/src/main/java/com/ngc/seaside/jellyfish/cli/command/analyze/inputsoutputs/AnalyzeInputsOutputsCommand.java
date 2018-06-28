package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

public class AnalyzeInputsOutputsCommand extends AbstractJellyfishAnalysisCommand {

   public static final String NAME = "analyze-inputs-outputs";

   // REMOVE THIS
   private ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> jellyFishCommandProvider;

   public AnalyzeInputsOutputsCommand() {
      super(NAME);
   }

   @Override
   public void activate() {
      logService.debug(AnalyzeInputsOutputsCommand.class, "Activated.");
   }

   @Override
   public void deactivate() {
      logService.debug(AnalyzeInputsOutputsCommand.class, "Deactivated.");
   }

   public void setJellyFishCommandProvider(ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> ref) {
      this.jellyFishCommandProvider = ref;
   }

   public void removeJellyFishCommandProvider(ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> ref) {
      setJellyFishCommandProvider(null);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that models which have inputs also have outputs.",
                              CommonParameters.MODEL,
                              CommonParameters.STEREOTYPES);
   }

   @Override
   protected void analyzeModel(IModel model) {
      if (!model.getInputs().isEmpty() && model.getOutputs().isEmpty()) {
         // Make this message an action message for fixing the issue.  This seems to be the pattern for the built
         // in Sonarqube rules.
         String message = "Add one or more outputs to the component.";
         ISourceLocation location = sourceLocatorService.getLocation(model, false);
         SystemDescriptorFinding<?> finding =
               InputsOutputsFindingTypes.INPUTS_WITH_NO_OUTPUTS.createFinding(message, location, 1);
         reportFinding(finding);
      }
   }

   @Override
   protected void doRun() {
      super.doRun();
      // This is temporary.  For testing only.
      jellyFishCommandProvider.run(new String[]{"console-report"});
   }
}
