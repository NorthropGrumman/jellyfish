package com.ngc.seaside.bootstrap.service.parameter.api;

import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

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
    * @throws ParameterServiceException If the format of the given parameters is invalid.
    *                                   the exception will be thrown
    *                                   if the parameter doesn't start with -D or
    *                                   if the parameter doesn't contain a
    *                                   value.
    */
   IParameterCollection parseParameters(List<String> parameters) throws ParameterServiceException;

   Set<String> getRequiredParameters();

   void setRequiredParameters(Set<String> newRequiredParameters);
}
