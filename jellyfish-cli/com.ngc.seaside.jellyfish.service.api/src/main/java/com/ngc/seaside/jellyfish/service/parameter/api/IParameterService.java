/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
