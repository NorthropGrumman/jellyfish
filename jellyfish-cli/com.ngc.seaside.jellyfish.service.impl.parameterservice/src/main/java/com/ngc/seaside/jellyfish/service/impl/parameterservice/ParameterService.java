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
package com.ngc.seaside.jellyfish.service.impl.parameterservice;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.jellyfish.service.parameter.api.ParameterServiceException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of the {@link IParameterService}
 */
@Component(service = IParameterService.class)
public class ParameterService implements IParameterService {

   private static final Pattern PATTERN = Pattern.compile("(-D)?((?:\\\\.|[^=,]+)*)=(\\S+)");
   private static final int NAME_GROUP = 2;
   private static final int VALUE_GROUP = 3;

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "deactivated");
   }

   @Override
   public IParameterCollection parseParameters(List<String> parameters)
         throws ParameterServiceException {
      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for (String eachParameterArg : parameters) {
         Matcher matcher = PATTERN.matcher(eachParameterArg);
         if (matcher.matches()) {
            String name = matcher.group(NAME_GROUP);
            String value = matcher.group(VALUE_GROUP);
            parameterCollection.addParameter(new DefaultParameter<>(name, value).required());
         } else {
            logService.warn(getClass(), "Unable to parse parameter '%s'", eachParameterArg);
         }
      }
      return parameterCollection;
   }

   @Override
   public IParameterCollection parseParameters(Map<String, ?> parameters) {
      //if they are empty, we will just return an empty collection. but a null parameter being passed in might
      //not be the intended outcome.
      Preconditions.checkNotNull(parameters, "The input parameters must not be null.");

      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for (Map.Entry<String, ?> entry : parameters.entrySet()) {
         parameterCollection.addParameter(new DefaultParameter<>(entry.getKey(), entry.getValue()).advanced());
      }

      return parameterCollection;
   }

   @Override
   public IParameterCollection getUnsetRequiredParameters(
         IUsage usage, IParameterCollection collection) {
      DefaultParameterCollection parameterCollection = new DefaultParameterCollection();
      for (IParameter<?> param : usage.getRequiredParameters()) {
         if (!collection.containsParameter(param.getName())) {
            parameterCollection.addParameter(param);
         }
      }
      return parameterCollection;
   }

   @Override
   public boolean isUsageSatisfied(IUsage usage, IParameterCollection collection) {
      return getUnsetRequiredParameters(usage, collection).isEmpty();
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }
}
