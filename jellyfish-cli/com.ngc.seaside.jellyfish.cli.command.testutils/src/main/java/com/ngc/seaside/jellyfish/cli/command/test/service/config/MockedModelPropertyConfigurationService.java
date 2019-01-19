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
package com.ngc.seaside.jellyfish.cli.command.test.service.config;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.IModelPropertyConfigurationService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * A basic abstract implementation of {@link IModelPropertyConfigurationService} for tests.
 */
public abstract class MockedModelPropertyConfigurationService<T> implements IModelPropertyConfigurationService<T> {

   private Map<String, Collection<T>> configurations = new LinkedHashMap<>();
   
   @Override
   public Collection<T> getConfigurations(IJellyFishCommandOptions options, IModel model) {
      return configurations.getOrDefault(model.getName(), Collections.emptySet());
   }

   protected <E extends T> E addConfiguration(IModel model, E configuration) {
      configurations.computeIfAbsent(model.getName(), __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }
   
}
