/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.osgi.service.component.annotations.Reference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An analysis that checks for that all scenarios have a corresponding feature and vice versa.
 */
public class AnalyzeFeaturesCommand extends AbstractJellyfishAnalysisCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze-features";

   private IFeatureService featureService;
   private Collection<IFeatureInformation> features;
   private Set<String> scenariosWithFeatures;

   /**
    * Creates a new command.
    */
   public AnalyzeFeaturesCommand() {
      super(NAME);
   }

   @Override
   public void activate() {
      logService.debug(AnalyzeFeaturesCommand.class, "Activated.");
   }

   @Override
   public void deactivate() {
      logService.debug(AnalyzeFeaturesCommand.class, "Deactivated.");
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that scenarios have a corresponding feature file and vice version.",
               CommonParameters.MODEL,
               CommonParameters.STEREOTYPES);
   }

   @Override
   protected void analyzeModel(IModel model) {
      for (IScenario scenario : model.getScenarios()) {
         String id = getScenarioId(scenario);
         if (!scenariosWithFeatures.contains(id)) {
            addScenarioMissingFeature(scenario);
         }
      }
   }

   @Override
   public void preAnalysis() {
      features = featureService.getAllFeatures(getOptions());
      scenariosWithFeatures = new HashSet<>();
      for (IFeatureInformation feature : features) {
         feature.getScenario()
                  .map(AnalyzeFeaturesCommand::getScenarioId)
                  .ifPresent(scenariosWithFeatures::add);
      }
   }

   @Override
   protected void analyzeEntireProject() {
      super.analyzeEntireProject();
      for (IFeatureInformation feature : features) {
         if (!feature.getScenario().isPresent()) {
            addFeatureMissingScenario(feature);
         }
      }
   }

   private static final String getScenarioId(IScenario scenario) {
      return scenario.getParent().getFullyQualifiedName() + "." + scenario.getName();
   }

   private void addFeatureMissingScenario(IFeatureInformation feature) {
      String message =
               "Feature " + feature.getPath().getFileName().toString() + " does not correspond to any scenario.";
      SystemDescriptorFinding<?> finding = FeatureFindingTypes.FEATURE_WITHOUT_SCENARIO.createFinding(message, null, 2);
      reportFinding(finding);
   }

   private void addScenarioMissingFeature(IScenario scenario) {
      String message = "Scenario " + scenario.getName() + " in model " + scenario.getParent().getFullyQualifiedName()
               + " does not have a feature file.";
      ISourceLocation location = sourceLocatorService.getLocation(scenario, false);
      SystemDescriptorFinding<?> finding =
               FeatureFindingTypes.SCENARIO_WITHOUT_FEATURE.createFinding(message, location, 2);
      reportFinding(finding);
   }

   @Reference
   public void setFeatureService(IFeatureService ref) {
      this.featureService = ref;
   }

   public void removeFeatureService(IFeatureService ref) {
      setFeatureService(null);
   }
}
