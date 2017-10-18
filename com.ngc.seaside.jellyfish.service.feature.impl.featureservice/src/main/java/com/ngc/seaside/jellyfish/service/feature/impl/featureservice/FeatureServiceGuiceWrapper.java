package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.nio.file.Path;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

public class FeatureServiceGuiceWrapper implements IFeatureService {

   private final FeatureService delegate;
   
   @Inject
   public FeatureServiceGuiceWrapper(ILogService logService) {
      delegate = new FeatureService();
      delegate.setLogService(logService);
   }

   @Override
   public NavigableMap<String, IFeatureInformation> getFeatures(Path sdPath, IModel model) {
      return delegate.getFeatures(sdPath, model);
   }

   @Override
   public NavigableMap<String, IFeatureInformation> getAllFeatures(Path sdPath, Collection<IModel> models) {
      return delegate.getAllFeatures(sdPath, models);
   }

   @Override
   public NavigableMap<String, IFeatureInformation> getFeatures(Path sdPath, IScenario scenario) {
      return delegate.getFeatures(sdPath, scenario);
   }
}
