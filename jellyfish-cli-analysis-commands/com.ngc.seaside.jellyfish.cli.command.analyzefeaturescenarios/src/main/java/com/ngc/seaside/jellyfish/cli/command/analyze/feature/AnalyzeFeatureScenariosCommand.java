/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An analysis that checks that all scenarios in a feature are uniquely named.
 */
public class AnalyzeFeatureScenariosCommand extends AbstractJellyfishAnalysisCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze-duplicate-feature-scenarios";

   /**
    * Creates a new command.
    */
   @Inject
   public AnalyzeFeatureScenariosCommand(ILogService logService,
                                         IAnalysisService analysisService,
                                         ISourceLocatorService sourceLocatorService) {
      super(NAME);
      setLogService(logService);
      setAnalysisService(analysisService);
      setSourceLocatorService(sourceLocatorService);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that scenarios in a feature file are uniquely named.");
   }

   @Override
   protected void analyzeFeature(IFeature feature) {
      Map<String, List<IGherkinScenario>> scenarioNames = new HashMap<>();
      for (IGherkinScenario scenario : feature.getScenarios()) {
         String name = scenario.getName();
         scenarioNames.computeIfAbsent(name, __ -> new ArrayList<>()).add(scenario);
      }
      for (Entry<String, List<IGherkinScenario>> entry : scenarioNames.entrySet()) {
         List<IGherkinScenario> scenarios = entry.getValue();
         if (scenarios.size() > 1) {
            duplicateScenarios(entry.getKey(), scenarios);
         }
      }
   }

   private void duplicateScenarios(String name, List<IGherkinScenario> scenarios) {
      String message = "There are multiple scenarios with the name \"" + name + "\"";
      ISourceLocation location = sourceLocatorService.getLocation(scenarios.iterator().next(), false);
      SystemDescriptorFinding<?> finding =
               FeatureScenariosFindingTypes.FEATURE_WITH_DUPLICATE_SCENARIOS.createFinding(message, location, 1);
      reportFinding(finding);
   }

}
