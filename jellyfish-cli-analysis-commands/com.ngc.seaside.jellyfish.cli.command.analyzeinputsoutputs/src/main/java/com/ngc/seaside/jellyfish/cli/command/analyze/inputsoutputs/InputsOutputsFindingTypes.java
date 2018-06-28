package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;

/**
 * Defines the different finding types for inputs/outputs analysis.
 */
public enum InputsOutputsFindingTypes implements ISystemDescriptorFindingType {

   // Use the singleton enum pattern for finding types.  Use the markdown files under the docs directory for the
   // descriptions.

   /**
    * The type of finding for inputs with no outputs.
    */
   INPUTS_WITH_NO_OUTPUTS("inputsWithNoOutputs",
                          "docs/inputsWithNoOutputs.md",
                          ISystemDescriptorFindingType.Severity.ERROR);

   private final String id;
   private final String description;
   private final Severity severity;

   InputsOutputsFindingTypes(String id,
                             String resource,
                             Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, InputsOutputsFindingTypes.class);
      this.severity = severity;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public Severity getSeverity() {
      return severity;
   }
}
