package com.ngc.seaside.command.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of the {@link IParameterCollection} interface that is backed by a
 * {@link java.util.LinkedHashMap}
 */
public class DefaultParameterCollection implements IParameterCollection {

   private Map<String, IParameter<?>> parameterMap = new LinkedHashMap<>();

   @Override
   public boolean isEmpty() {
      return parameterMap.isEmpty();
   }

   @Override
   public boolean containsParameter(String parameterName) {
      return parameterMap.containsKey(parameterName);
   }

   @Override
   public IParameter getParameter(String parameterName) {
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
    */
   public void addParameter(IParameter<?> parameter) {
      parameterMap.put(parameter.getName(), parameter);
   }

   /**
    * Add the given parameters to the collection in the order that they are in the list.
    *
    * @param parameters the parameters to add.
    */
   public void addParameters(List<IParameter<?>> parameters) {
      parameters.forEach(this::addParameter);
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
