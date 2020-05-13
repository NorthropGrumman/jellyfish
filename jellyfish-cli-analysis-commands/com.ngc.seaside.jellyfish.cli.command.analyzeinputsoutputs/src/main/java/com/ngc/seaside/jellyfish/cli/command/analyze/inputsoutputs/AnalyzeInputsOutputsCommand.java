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
                              CommonParameters.MODEL.required(),
                              CommonParameters.STEREOTYPES.optional());
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
