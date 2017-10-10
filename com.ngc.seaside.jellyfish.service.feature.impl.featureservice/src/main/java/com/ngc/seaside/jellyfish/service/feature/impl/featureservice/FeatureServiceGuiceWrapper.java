package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;

public class FeatureServiceGuiceWrapper implements IFeatureService {

   private final FeatureService delegate;
   
   @Inject
   public FeatureServiceGuiceWrapper(ILogService logService) {
      delegate = new FeatureService();
      delegate.setLogService(logService);
   }

   @Override
   public IFeatureInformation getFeatureInfo(String uri) {
      return delegate.getFeatureInfo(uri);
   }
}
