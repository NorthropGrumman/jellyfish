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
