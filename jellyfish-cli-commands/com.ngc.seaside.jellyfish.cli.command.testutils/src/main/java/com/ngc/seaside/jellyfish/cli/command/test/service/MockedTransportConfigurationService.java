package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
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
   private Map<String, Collection<TelemetryConfiguration>> telemetryConfigurations = new LinkedHashMap<>();

   /**
    * Creates a multicast configuration.
    *
    * @param fieldName    the field name
    * @param groupAddress the group address
    * @param port         the port number
    * @param sourceName   the source name
    * @param targetName   the target name
    * @return a configured MulticastConfiguration
    */
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

   /**
    * Creates a rest configuration.
    *
    * @param fieldName     the field name
    * @param address       the address
    * @param interfaceName the interface name
    * @param port          the port number
    * @param path          the path name
    * @param contentType   the content type
    * @param httpMethod    the http method
    * @return a configured RestConfiguration
    */
   public RestConfiguration addRestConfiguration(String fieldName, String address, String interfaceName, int port,
                                                 String path, String contentType, HttpMethod httpMethod) {
      RestConfiguration configuration =
            new RestConfiguration().setNetworkAddress(new NetworkAddress().setAddress(address))
                  .setNetworkInterface(new NetworkInterface().setName(interfaceName))
                  .setPort(port)
                  .setPath(path)
                  .setContentType(contentType)
                  .setHttpMethod(httpMethod);
      restConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }

   /**
    * Creates a ZeroMQ transport configuration.
    *
    * @param fieldName      the field name
    * @param connectionType the connection type
    * @param bind           the bind
    * @param connect        the connection address
    * @param port           the port number
    * @return a configured ZeroMqTcpTransportConfiguration
    */
   public ZeroMqTcpTransportConfiguration addZeroMqTcpConfiguration(String fieldName, ConnectionType connectionType,
                                                                    String bind, String connect, int port) {
      ZeroMqTcpTransportConfiguration
            configuration =
            (ZeroMqTcpTransportConfiguration) new ZeroMqTcpTransportConfiguration()
                  .setBindConfiguration(new NetworkInterface(bind))
                  .setConnectConfiguration(new NetworkAddress().setAddress(connect))
                  .setPort(port)
                  .setConnectionType(connectionType);
      zeroMqConfigurations.computeIfAbsent(fieldName, __ -> new LinkedHashSet<>()).add(configuration);
      return configuration;
   }

   /**
    * Creates a rest telemetry configuration.
    *
    * @param modelName     the model name
    * @param address       the address
    * @param interfaceName the interface name
    * @param port          the port number
    * @param path          the path name
    * @param contentType   the content type
    * @param httpMethod    the http method
    * @return a configured RestConfiguration
    */
   public RestTelemetryConfiguration addRestTelemetryConfiguration(String modelName, String address,
                                                                   String interfaceName, int port, String path, 
                                                                   String contentType, HttpMethod httpMethod) {
      RestConfiguration restConfiguration =
            new RestConfiguration().setNetworkAddress(new NetworkAddress().setAddress(address))
                  .setNetworkInterface(new NetworkInterface().setName(interfaceName))
                  .setPort(port)
                  .setPath(path)
                  .setContentType(contentType)
                  .setHttpMethod(httpMethod);
      RestTelemetryConfiguration configuration = new RestTelemetryConfiguration();
      configuration.setConfig(restConfiguration);
      telemetryConfigurations.computeIfAbsent(modelName, __ -> new LinkedHashSet<>()).add(configuration);
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
   public Collection<TelemetryConfiguration> getTelemetryConfiguration(IJellyFishCommandOptions options,
                                                                       IModel model) {
      return telemetryConfigurations.getOrDefault(model.getName(), Collections.emptySet());
   }

   @Override
   public Set<TransportConfigurationType> getConfigurationTypes(IJellyFishCommandOptions options, IModel model) {
      Set<TransportConfigurationType> types = new HashSet<>();
      if (!multicastConfigurations.isEmpty()) {
         types.add(TransportConfigurationType.MULTICAST);
      }
      if (!restConfigurations.isEmpty()) {
         types.add(TransportConfigurationType.REST);
      }
      if (!zeroMqConfigurations.isEmpty()) {
         types.add(TransportConfigurationType.ZERO_MQ);
      }
      if (!telemetryConfigurations.isEmpty()) {
         types.add(TransportConfigurationType.TELEMETRY);
      }
      return types;
   }

}
