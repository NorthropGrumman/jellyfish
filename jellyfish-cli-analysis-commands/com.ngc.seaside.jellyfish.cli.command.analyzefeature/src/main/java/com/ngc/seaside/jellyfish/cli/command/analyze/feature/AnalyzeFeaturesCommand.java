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

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

/**
 * An analysis that checks for that all scenarios have a corresponding feature and vice versa.
 */
public class AnalyzeFeaturesCommand extends AbstractJellyfishAnalysisCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze-features";

   /**
    * Creates a new command.
    */
   @Inject
   public AnalyzeFeaturesCommand(ILogService logService,
                                 IAnalysisService analysisService,
                                 ISourceLocatorService sourceLocatorService) {
      super(NAME);
      setLogService(logService);
      setAnalysisService(analysisService);
      setSourceLocatorService(sourceLocatorService);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that scenarios have a corresponding feature file and vice versa. "
                              + "This command is rarely ran directly;"
                              + " instead it is run with the 'analyze' command.",
                              CommonParameters.MODEL.required(),
                              CommonParameters.STEREOTYPES.optional());
   }

   @Override
   protected void analyzeModel(IModel model) {
      IGherkinParsingResult gherkinResult = getOptions().getGherkinParsingResult();
      if (gherkinResult.isSuccessful()) {
         // Require all SD scenarios to have feature files.
         for (IScenario scenario : model.getScenarios()) {
            if (!gherkinResult.findFeature(scenario).isPresent()) {
               missingFeatureFileForSdScenario(scenario);
            }
         }
      }
   }

   @Override
   protected void analyzeEntireProject() {
      IGherkinParsingResult gherkinResult = getOptions().getGherkinParsingResult();
      if (gherkinResult.isSuccessful()) {
         super.analyzeEntireProject();
         // Require all feature files to have an SD scenario.
         for (IFeature feature : gherkinResult.getFeatures()) {
            if (!feature.getModelScenario().isPresent()) {
               missingSdScenarioForFeatureFile(feature);
            }
         }
      }
   }

   private void missingFeatureFileForSdScenario(IScenario scenario) {
      String message = "Scenario "
                       + scenario.getName()
                       + " in model "
                       + scenario.getParent().getFullyQualifiedName()
                       + " does not have a feature file.";
      ISourceLocation location = sourceLocatorService.getLocation(scenario, false);
      SystemDescriptorFinding<?> finding = FeatureFindingTypes.SCENARIO_WITHOUT_FEATURE.createFinding(message,
                                                                                                      location,
                                                                                                      2);
      reportFinding(finding);
   }

   private void missingSdScenarioForFeatureFile(IFeature feature) {
      String msg = "Feature "
                   + feature.getFullyQualifiedName()
                   + " does not have a corresponding System Descriptor scenario.";
      ISourceLocation location = sourceLocatorService.getLocation(feature, false);
      SystemDescriptorFinding<?> finding = FeatureFindingTypes.FEATURE_WITHOUT_SCENARIO.createFinding(msg, location, 2);
      reportFinding(finding);
   }
}
