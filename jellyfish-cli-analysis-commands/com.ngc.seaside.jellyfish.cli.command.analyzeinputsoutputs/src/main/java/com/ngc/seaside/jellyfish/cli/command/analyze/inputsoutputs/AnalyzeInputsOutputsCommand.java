package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

public class AnalyzeInputsOutputsCommand extends AbstractJellyfishAnalysisCommand {

   public static final String NAME = "analyze-inputs-outputs";

   private IAnalysisService analysisService;

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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeAnalysisService")
   public void setAnalysisService(IAnalysisService ref) {
      this.analysisService = ref;
   }

   public void removeAnalysisService(IAnalysisService ref) {
      setAnalysisService(null);
   }

   @Override
   protected void doRun() {
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that models which have inputs also have outputs.",
                              CommonParameters.MODEL);
   }

}
