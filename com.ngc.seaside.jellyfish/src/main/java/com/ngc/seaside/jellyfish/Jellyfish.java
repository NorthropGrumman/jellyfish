package com.ngc.seaside.jellyfish;

import com.ngc.blocs.service.log.api.ILogService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {}, immediate = true)
public class Jellyfish {

   private ILogService logService;

   @Activate
   public void activate() {
      logService.info(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.info(getClass(), "deactivated");
   }

   @Reference(
     unbind="removeLogService",
     cardinality = ReferenceCardinality.MANDATORY, 
     policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }
}
