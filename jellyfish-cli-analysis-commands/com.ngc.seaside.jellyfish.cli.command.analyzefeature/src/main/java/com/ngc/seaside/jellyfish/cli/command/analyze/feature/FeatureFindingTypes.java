package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;

/**
 * Defines the different finding types for feature analysis.
 */
public enum FeatureFindingTypes implements ISystemDescriptorFindingType {

   /**
    * The type of finding for features found without a corresponding scenario.
    */
   FEATURE_WITHOUT_SCENARIO("featureWithoutScenario",
            "docs/modelWithoutFeature.md",
            Severity.WARNING),

   /**
    * The type of finding for scenarios without a corresponding feature file.
    */
   SCENARIO_WITHOUT_FEATURE("scenarioWithoutFeature",
            "docs/modelWithoutFeature.md",
            Severity.WARNING);

   private final String id;
   private final String description;
   private final Severity severity;

   FeatureFindingTypes(String id,
                       String resource,
                       Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, FeatureFindingTypes.class);
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
