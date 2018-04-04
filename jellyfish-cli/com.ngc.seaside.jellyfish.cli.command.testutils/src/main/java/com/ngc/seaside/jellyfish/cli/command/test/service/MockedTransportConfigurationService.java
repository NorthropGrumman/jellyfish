package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
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
   private Map<String, Collection<RestConfiguration>> restConfigurations = new LinkedHashMap<>();
   private Map<String, Collection<ZeroMqConfiguration>> zeroMqConfigurations = new LinkedHashMap<>();

   public MulticastConfiguration addMulticastConfiguration(String fieldName, String groupAddress, int port,
                                                           String sourceName, String targetName) {
      NetworkInterface targetInterface = new NetworkInterface(targetName);
      NetworkInterface sourceInterface = new NetworkInterface(sourceName);

      MulticastConfiguration configuration = new MulticastConfiguration().setGroupAddress(groupAddress)
                                                                         .setSourceInterface(sourceInterface)
                                                                         .setTargetInterface(targetInterface)
                                                                         .setPort(port);
      multicastConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }

   public RestConfiguration addRestConfiguration(String fieldName, String serverAddress, String serverInterface,
                                                 int port, String path, String contentType, HttpMethod method) {
      RestConfiguration configuration = new RestConfiguration()
               .setNetworkAddress(new NetworkAddress().setAddress(serverAddress))
               .setNetworkInterface(new NetworkInterface(serverInterface))
               .setPort(port)
               .setPath(path)
               .setContentType(contentType)
               .setHttpMethod(method);
      restConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }

   public ZeroMqTcpTransportConfiguration addZeroMqTcpConfiguration(String fieldName, ConnectionType connectType, 
                                                                    String bind, String connect, int port) {
      ZeroMqTcpTransportConfiguration configuration = (ZeroMqTcpTransportConfiguration) new ZeroMqTcpTransportConfiguration()
               .setBindConfiguration(new NetworkInterface(bind))
               .setConnectConfiguration(new NetworkAddress().setAddress(connect))
               .setPort(port)
               .setConnectionType(connectType);
      zeroMqConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
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
      return restConfigurations.getOrDefault(field.getName(), Collections.emptySet());
   }

   @Override
   public Collection<ZeroMqConfiguration> getZeroMqConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return zeroMqConfigurations.getOrDefault(field.getName(), Collections.emptySet());
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
