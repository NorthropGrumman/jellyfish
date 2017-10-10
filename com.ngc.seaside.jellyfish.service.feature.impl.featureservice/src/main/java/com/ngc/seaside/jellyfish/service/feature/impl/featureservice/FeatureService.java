package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.jellyfish.service.feature.api.dto.FeatureDto;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IFeatureService.class)
public class FeatureService implements IFeatureService {

   private ILogService logService;
   
   @Override
   public FeatureDto getFeatureInfo(String uri) {
      //TODO implement me
      return null;
   }
   
   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }



}
