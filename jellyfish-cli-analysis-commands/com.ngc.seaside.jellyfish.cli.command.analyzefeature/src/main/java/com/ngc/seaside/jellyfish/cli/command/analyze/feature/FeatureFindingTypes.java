/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
