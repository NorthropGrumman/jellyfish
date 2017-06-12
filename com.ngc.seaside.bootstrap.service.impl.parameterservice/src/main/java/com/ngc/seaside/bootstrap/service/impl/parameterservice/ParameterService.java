package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameterCollection;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by J55690 on 6/7/2017.
 */
public class ParameterService implements IParameterService {

   private ILogService logService;
   private static final String PATTERN = "^-D[\\w]+=[\\w\\d]+$";

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

      // TODO populate required Parameters list.
      List<String> requiredParameters = new ArrayList<>();

      DefaultParameterCollection pc = new DefaultParameterCollection();
      for (String eachParameterArg : parameters) {
         boolean isValid = validateParameter(eachParameterArg);
         if (!isValid) {
            throw new ParameterServiceException(
                  "Invalid Argument: " + eachParameterArg + ". Expected format: " + PATTERN);
         }

         String name = eachParameterArg.split("=")[0].substring(2);
         String value = eachParameterArg.split("=")[1];

         //TODO To read JellyFish properties to determine if parameters are required.
         DefaultParameter eachParameter = new DefaultParameter(name, true);
         eachParameter.setValue(value);
         pc.addParameter(eachParameter);
      }

      return pc;
   }

   private boolean validateParameter(String parameter) {

      Pattern r = Pattern.compile(PATTERN);
      Matcher m = r.matcher(parameter);

      return m.matches();
   }
}
