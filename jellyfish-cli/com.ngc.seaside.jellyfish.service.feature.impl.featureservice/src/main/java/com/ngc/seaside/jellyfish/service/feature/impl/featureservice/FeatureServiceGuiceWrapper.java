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
package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;

public class FeatureServiceGuiceWrapper implements IFeatureService {

   private final FeatureService delegate;

   @Inject
   public FeatureServiceGuiceWrapper() {
      delegate = new FeatureService();
   }

   @Override
   public Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IModel model) {
      return delegate.getFeatures(options, model);
   }

   @Override
   public Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IScenario scenario) {
      return delegate.getFeatures(options, scenario);
   }

   @Override
   public Collection<IFeatureInformation> getAllFeatures(IJellyFishCommandOptions options) {
      return delegate.getAllFeatures(options);
   }
}
