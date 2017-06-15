package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Component;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by J55690 on 6/7/2017.
 */
@Component(service = IParameterService.class)
public class ParameterService implements IParameterService {

   //private static final String PATTERN = "^-D[a-zA-Z]+=[\\S]+$";

   @Override
   public IParameterCollection parseParameters(IUsage usage, List<String> parameters) throws ParameterServiceException {
      DefaultParameterCollection pc = new DefaultParameterCollection();

      // Construct required parameters from usage data
      LinkedHashSet<String> requiredParameters = new LinkedHashSet<>();
      for (IParameter param: usage.getRequiredParameters()) {
         requiredParameters.add(param.getName());
      }

      // Parse params then construct IParameters
      for (String eachParameterArg : parameters) {
         //validateParameter(eachParameterArg);

         String name = eachParameterArg.split("=")[0].substring(2);
         String value = eachParameterArg.split("=")[1];

         DefaultParameter eachParameter;
         if (requiredParameters.contains(name)){
            eachParameter = new DefaultParameter(name, true);
         } else {
            eachParameter = new DefaultParameter(name, false);
         }
         eachParameter.setValue(value);
         pc.addParameter(eachParameter);
      }

      validateRequiredParameters(requiredParameters, pc);

      return pc;
   }

   private void validateRequiredParameters(LinkedHashSet<String> requiredParameters, DefaultParameterCollection parameterCollection)
            throws ParameterServiceException {
      for (String eachRequiredParameter : requiredParameters) {
         if (!parameterCollection.containsParameter(eachRequiredParameter)) {
            throw new ParameterServiceException(
                     "Required parameter not found: " + eachRequiredParameter);
         }

      }
   }

//   private void validateParameter(String parameter) throws ParameterServiceException {
//      Pattern r = Pattern.compile(PATTERN);
//      Matcher m = r.matcher(parameter);
//
//      if (!m.matches()) {
//         throw new ParameterServiceException(
//                  "Invalid Argument: " + parameter + ". Expected format: " + PATTERN);
//      }
//   }
}
