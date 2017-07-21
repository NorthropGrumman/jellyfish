package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of the {@link IParameterService}
 */
@Component(service = IParameterService.class)
public class ParameterService implements IParameterService {

   private static final String PATTERN =
         "-D((?:\\\\.|[^=,]+)*)=(\"(?:\\\\.|[^\"\\\\]+)*\"|(?:\\\\.|[^,\"\\\\]+)*)";
   private static final int NAME_INDEX = 0;
   private static final int VALUE_INDEX = 1;

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "deactivated");
   }

   @Override
   public IParameterCollection parseParameters(List<String> parameters)
         throws ParameterServiceException {
      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for (String eachParameterArg : parameters) {
         if (isValidParameterFormat(eachParameterArg)) {
            //remove the -D from the name
            String name = eachParameterArg.split("=")[NAME_INDEX].substring(2).trim();
            //take everything after the equals for the value
            String value = eachParameterArg.split("=")[VALUE_INDEX].trim();

            parameterCollection.addParameter(new DefaultParameter<>(name, value));
         } else {
            logService.warn(getClass(), "Unable to parse parameter '%s'", eachParameterArg);
         }
      }
      return parameterCollection;
   }

   @Override
   public IParameterCollection parseParameters(Map<String, ?> parameters) {
      //if they are empty, we will just return an empty collection. but a null parameter being passed in might
      //not be the intended outcome.
      Preconditions.checkNotNull(parameters, "The input parameters must not be null.");

      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for(Map.Entry<String, ?> entry : parameters.entrySet()) {
         parameterCollection.addParameter(new DefaultParameter<>(entry.getKey(), entry.getValue()));
      }

      return parameterCollection;
   }

   @Override
   public IParameterCollection getUnsetRequiredParameters(
         IUsage usage, IParameterCollection collection) {
      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for (IParameter param : usage.getRequiredParameters()) {
         if(!collection.containsParameter(param.getName())) {
            parameterCollection.addParameter(param);
         }
      }
      return parameterCollection;
   }

   @Override
   public boolean isUsageSatisfied(IUsage usage, IParameterCollection collection) {
      return getUnsetRequiredParameters(usage, collection).isEmpty();
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Determine if the given parameter follows the format -Dkey=value
    *
    * @param parameter the parameter.
    */
   private boolean isValidParameterFormat(String parameter) {
      Pattern r = Pattern.compile(PATTERN);
      Matcher m = r.matcher(parameter);

      return m.matches();
   }


}
