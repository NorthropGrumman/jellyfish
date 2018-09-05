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
