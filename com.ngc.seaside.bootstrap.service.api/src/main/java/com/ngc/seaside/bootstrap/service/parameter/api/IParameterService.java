package com.ngc.seaside.bootstrap.service.parameter.api;

import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import java.util.List;
import java.util.Set;

/**
 * The purpose of this interface is to allow for passing in parameters in a collection of Strings in the
 * -Dproperty=value format. The service will then produce a collection of IParameter values.
 *
 * @see IParameter
 */
public interface IParameterService {

   /**
    * Parse a collection of String objects in the format -Dproperty=value
    *
    * @param parameters the list of parameters as strings.
    * @return The collection of parameters
    * @throws ParameterServiceException If the format of the given parameters is invalid. the exception will be thrown
    *                                   if the parameter doesn't start with -D or if the parameter doesn't contain a
    *                                   value.
    */
   IParameterCollection parseParameters(IUsage usage, List<String> parameters) throws ParameterServiceException;

   /**
    * Returns a set of required parameters belonging to this service.
    *
    * @return A set of required parameters names
    */
   Set<String> getRequiredParameters();

   /**
    * Sets the required parameters names for this service which aids in validation and creation of individual
    * IParameters. The Required parameters must be passed in for the service to operate.
    *
    * @param newRequiredParameters the set of required parameters of which this service must have.
    */
   void setRequiredParameters(Set<String> newRequiredParameters);
}
