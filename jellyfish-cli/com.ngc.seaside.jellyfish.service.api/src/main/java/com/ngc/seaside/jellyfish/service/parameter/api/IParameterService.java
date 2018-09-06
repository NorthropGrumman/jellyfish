/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.parameter.api;

import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;

import java.util.List;
import java.util.Map;

/**
 * The purpose of this interface is to allow for passing in parameters in a collection of Strings in the
 * -Dproperty=value format. The service will then produce a collection of IParameter values.
 *
 * @see IParameter
 */
public interface IParameterService {

   /**
    * Parse the list of parameters in the format of -Dkey=value
    *
    * @param parameters the parameters.
    * @return the collection of parameters. The collection will be empty if the format is incorrect.
    */
   IParameterCollection parseParameters(List<String> parameters);

   /**
    * Parse the map of parameters. This is similar to the above method but the key value pairs are already
    * separated.
    *
    * @param parameters the map of key value pairs.
    * @return the collection.
    */
   IParameterCollection parseParameters(Map<String, ?> parameters);

   /**
    * Determine if the usage is satisfied by the given collection. This means that all the required
    * parameters in the usage are present in the collection.
    * It would be normal usage to call this method after calling {@link #parseParameters(List)}
    *
    * @param usage      the usage.
    * @param collection the parameters.
    * @return true if the usage is satisfied.
    */
   boolean isUsageSatisfied(IUsage usage, IParameterCollection collection);

   /**
    * Get the parameters that are required but that are not present in the
    * given parameter collection.
    *
    * @param usage      the usage.
    * @param collection the collection.
    * @return the collection or an empty collection if the usage is satisfied.
    */
   IParameterCollection getUnsetRequiredParameters(IUsage usage, IParameterCollection collection);
}
