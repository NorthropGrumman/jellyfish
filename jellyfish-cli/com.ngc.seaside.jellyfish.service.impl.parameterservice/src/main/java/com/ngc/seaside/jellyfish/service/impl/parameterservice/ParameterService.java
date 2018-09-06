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
            parameterCollection.addParameter(new DefaultParameter<>(name, value));
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
         parameterCollection.addParameter(new DefaultParameter<>(entry.getKey(), entry.getValue()));
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
