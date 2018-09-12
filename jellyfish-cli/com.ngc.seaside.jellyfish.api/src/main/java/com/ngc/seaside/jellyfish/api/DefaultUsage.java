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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Simple object for storing the attributes of the IUsage interface.
 *
 * @author justan.provence@ngc.com
 */
public class DefaultUsage implements IUsage {

   private String description;
   private List<IParameter<?>> allParameters = new ArrayList<>();
   private List<IParameter<?>> requiredParameters = new ArrayList<>();

   /**
    * Constructor.
    *
    * @param parameters Array of parameters without any brackets. Can be null.
    */
   public DefaultUsage(String description, IParameter<?>... parameters) {
      this.description = description;
      if (parameters != null) {
         this.allParameters = Arrays.asList(parameters);
         for (IParameter<?> param : this.allParameters) {
            if (param.getParameterCategory() == ParameterCategory.REQUIRED) {
               requiredParameters.add(param);
            }
         }
      }
   }

   /**
    * Constructor.
    *
    * @param parameters List of parameters. Can be null.
    */
   public DefaultUsage(String description, List<IParameter<?>> parameters) {
      this.description = description;
      if (parameters != null) {
         this.allParameters = parameters;   
         for (IParameter<?> param : this.allParameters) {
            if (param.getParameterCategory() == ParameterCategory.REQUIRED) {
               requiredParameters.add(param);
            }
         }
      } else {
         this.allParameters = new ArrayList<>();
      }
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public List<IParameter<?>> getAllParameters() {
      return allParameters;
   }

   @Override
   public List<IParameter<?>> getRequiredParameters() {
      return requiredParameters;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(description).append(" ");
      for (IParameter<?> parameter : allParameters) {
         builder.append(String.format("[%s] ", parameter));
      }
      return builder.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IUsage)) {
         return false;
      }
      IUsage that = (IUsage) obj;
      return Objects.equals(description, that.getDescription())
             && Objects.equals(allParameters, that.getAllParameters());
   }

   @Override
   public int hashCode() {
      return Objects.hash(description, allParameters);
   }
}
