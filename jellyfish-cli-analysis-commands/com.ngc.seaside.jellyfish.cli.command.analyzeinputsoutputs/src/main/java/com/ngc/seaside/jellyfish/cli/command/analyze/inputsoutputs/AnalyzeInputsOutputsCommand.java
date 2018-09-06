package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

/**
 * An analysis that checks for models which contain inputs but no outputs.  See
 * http://10.166.134.55/confluence/display/SEAS/Ch.+1+Avoid+components+that+have+inputs+but+no+outputs for details.
 */
public class AnalyzeInputsOutputsCommand extends AbstractJellyfishAnalysisCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze-inputs-outputs";

   /**
    * Creates a new command.
    */
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

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that models which have inputs also have outputs",
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
}
