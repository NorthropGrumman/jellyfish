package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of {@link IRequirementsService} for tests. The actual metadata is never searched for requirements; rather, requirements can be mocked nusing
 * {@link #addRequirements(Object, String...)}.
 */
public class MockedRequirementsService implements IRequirementsService {

   private final Map<Object, Set<String>> map = new IdentityHashMap<>();

   public MockedRequirementsService addRequirements(Object element, String... requirements) {
      map.computeIfAbsent(element, __ -> new TreeSet<>()).addAll(Arrays.asList(requirements));
      return this;
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IMetadata metadata) {
      return map.getOrDefault(metadata, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IReferenceField field) {
      return map.getOrDefault(field, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IDataField field) {
      return map.getOrDefault(field, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IData data) {
      return map.getOrDefault(data, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IEnumeration enumeration) {
      return map.getOrDefault(enumeration, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IModel model) {
      return map.getOrDefault(model, Collections.emptySet());
   }

   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IScenario scenario) {
      return map.getOrDefault(scenario, Collections.emptySet());
   }

}
