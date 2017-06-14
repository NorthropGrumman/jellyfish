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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by J55690 on 6/7/2017.
 */
public class ParameterService implements IParameterService {

   private static final String PATTERN = "^-D[\\w]+=[.]+$";

   private ILogService logService;
   private final LinkedHashSet<String> requiredParameters = new LinkedHashSet<>();

   @Override
   public Set<String> getRequiredParameters() {
      return new LinkedHashSet<>(requiredParameters);
   }

   @Override
   public void setRequiredParameters(Set<String> newRequiredParameters) {
      this.requiredParameters.addAll(newRequiredParameters);
   };

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
      DefaultParameterCollection pc = new DefaultParameterCollection();

      for (String eachParameterArg : parameters) {
         validateParameter(eachParameterArg);

         String name = eachParameterArg.split("=")[0].substring(2);
         String value = eachParameterArg.split("=")[1];

         DefaultParameter eachParameter = new DefaultParameter(name, true);
         eachParameter.setValue(value);
         pc.addParameter(eachParameter);
      }

      validateRequiredParameters(pc);

      return pc;
   }

   private void validateRequiredParameters(DefaultParameterCollection parameterCollection) throws ParameterServiceException{
      for(String  eachRequiredParameter : requiredParameters) {
         if (!parameterCollection.containsParameter(eachRequiredParameter)){
            throw new ParameterServiceException(
                  "Required parameter not found: " + eachRequiredParameter);
         }

      }
   }

   private void validateParameter(String parameter) throws ParameterServiceException {
      Pattern r = Pattern.compile(PATTERN);
      Matcher m = r.matcher(parameter);

      if(!m.matches()) {
         throw new ParameterServiceException(
               "Invalid Argument: " + parameter + ". Expected format: " + PATTERN);
      }
   }


//   /**
//    * Sets log service.
//    *
//    * @param ref the ref
//    */
//   @Reference(cardinality = ReferenceCardinality.MANDATORY,
//         policy = ReferencePolicy.STATIC,
//         unbind = "removeLogService")
//   public void setLogService(ILogService ref) {
//      this.logService = ref;
//   }
//
//   /**
//    * Remove log service.
//    */
//   public void removeLogService(ILogService ref) {
//      setLogService(null);
//   }
}
