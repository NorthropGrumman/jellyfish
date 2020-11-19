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
package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

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
