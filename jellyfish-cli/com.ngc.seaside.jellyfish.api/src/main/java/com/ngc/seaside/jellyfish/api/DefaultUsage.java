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
