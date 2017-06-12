package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.IParameterCollection;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;

import java.util.List;

/**
 * Created by J55690 on 6/7/2017.
 */
public class ParameterService implements IParameterService {

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "deactivated");
   }

   @Inject
   public void setLogService(ILogService ref) {
      this.setLogService(ref);
   }

   @Override
   public IParameterCollection parseParameters(List<String> parameters) throws ParameterServiceException {
       return null;

   }
}
