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
package com.ngc.seaside.jellyfish.api;

import java.util.List;
import java.util.Map;

/**
 * This provides a collection of parameters with convenience methods to look a parameter up by its name.
 * The {@link #getParameterMap()} method should be used sparingly since it will return a copy of the map that is
 * interface backs. This is to ensure that this class is immutable.
 */
public interface IParameterCollection {

   /**
    * Determine if the collection has any parameters.
    *
    * @return true if the collection is empty
    */
   boolean isEmpty();

   /**
    * Determine if the given parameter name exists within the collection.
    *
    * @param parameterName the name of the parameter {@link IParameter#getName()}
    * @return true if the collection contains a parameter of the given name.
    */
   boolean containsParameter(String parameterName);

   /**
    * Get the parameter given its name. In the even that the parameter doesn't exist this method will return null.
    * Use the {@link #containsParameter(String)} method to determine if the parameter exist before calling this method.
    *
    * @param parameterName the name of the parameter.
    * @return The parameter by the given name or null if the parameter was not found.
    */
   IParameter<?> getParameter(String parameterName);

   /**
    * Get an list of the parameters in the order in which the user entered them.
    * This method will return a copy of all the values in the collection.
    *
    * @return the list of parameters.
    */
   List<IParameter<?>> getAllParameters();

   /**
    * This method will allow you to get the parameters mapped by the parameter's name.
    * It is recommended you use the {@link #getParameter(String)} metho when possible since this method
    * will return a copy of the map.
    *
    * @return A map of the parameters by their name.
    * @see #getParameter(String)
    * @see #getAllParameters()
    */
   Map<String, IParameter<?>> getParameterMap();
}
