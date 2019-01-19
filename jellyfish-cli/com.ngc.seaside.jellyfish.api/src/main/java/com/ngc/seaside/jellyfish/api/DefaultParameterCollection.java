/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of the {@link IParameterCollection} interface that is backed by a {@link
 * java.util.LinkedHashMap}
 */
public class DefaultParameterCollection implements IParameterCollection {

   private Map<String, IParameter<?>> parameterMap = new LinkedHashMap<>();

   public DefaultParameterCollection() {
   }

   public DefaultParameterCollection(IParameterCollection collection) {
      parameterMap.putAll(collection.getParameterMap());
   }

   @Override
   public boolean isEmpty() {
      return parameterMap.isEmpty();
   }

   @Override
   public boolean containsParameter(String parameterName) {
      return parameterMap.containsKey(parameterName);
   }

   @Override
   public IParameter<?> getParameter(String parameterName) {
      return parameterMap.get(parameterName);
   }

   @Override
   public List<IParameter<?>> getAllParameters() {
      return new ArrayList<>(parameterMap.values());
   }

   @Override
   public Map<String, IParameter<?>> getParameterMap() {
      return new LinkedHashMap<>(parameterMap);
   }

   /**
    * Add parameters to the collection.
    *
    * @param parameter the parameter to add.
    * @return this collection
    */
   public DefaultParameterCollection addParameter(IParameter<?> parameter) {
      parameterMap.put(parameter.getName(), parameter);
      return this;
   }

   /**
    * Add the given parameters to the collection in the order that they are in the list.
    *
    * @param parameters the parameters to add.
    * @return this collection
    */
   public DefaultParameterCollection addParameters(List<IParameter<?>> parameters) {
      parameters.forEach(this::addParameter);
      return this;
   }

   @Override
   public String toString() {
      return String.format("parameterMap: %s", parameterMap);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IParameterCollection)) {
         return false;
      }
      IParameterCollection that = (IParameterCollection) obj;
      return Objects.equals(parameterMap, that.getParameterMap());
   }

   @Override
   public int hashCode() {
      return Objects.hash(parameterMap);
   }
}
