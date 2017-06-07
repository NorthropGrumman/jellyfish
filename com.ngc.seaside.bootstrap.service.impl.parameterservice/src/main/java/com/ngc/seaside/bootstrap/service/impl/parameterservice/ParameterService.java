package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.ngc.seaside.bootstrap.service.parameter.api.IParameterCollection;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;

import java.util.List;

/**
 * Created by J55690 on 6/7/2017.
 */
public class ParameterService implements IParameterService {

   @Override
   public IParameterCollection parseParameters(List<String> parameters) throws ParameterServiceException {
       return null;

      @Activate
      public void activate() {

      }

      @Deactivate
      public void deactivate() {

      }

   }
}
