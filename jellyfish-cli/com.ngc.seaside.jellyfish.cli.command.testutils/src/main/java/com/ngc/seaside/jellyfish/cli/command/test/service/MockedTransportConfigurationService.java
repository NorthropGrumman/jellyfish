package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MockedTransportConfigurationService implements ITransportConfigurationService {

   private Map<String, Collection<MulticastConfiguration>> multicastConfigurations = new LinkedHashMap<>();

   public MulticastConfiguration addMulticastConfiguration(String fieldName, String address, int port) {
      MulticastConfiguration configuration = new MulticastConfiguration().setAddress(address)
                                                                         .setPort(port);
      multicastConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      return field.getType().getName().toUpperCase();
   }

   @Override
   public Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return multicastConfigurations.getOrDefault(field.getName(), Collections.emptySet());
   }

   @Override
   public Collection<RestConfiguration> getRestConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      throw new UnsupportedOperationException("Not implemented");
   }

   @Override
   public Set<TransportConfigurationType> getConfigurationTypes(IJellyFishCommandOptions options,
            IModel deploymentModel) {
      Set<TransportConfigurationType> types = new HashSet<>();
      if (!multicastConfigurations.isEmpty()) {
         types.add(TransportConfigurationType.MULTICAST);
      }
      return types;
   }

}
