package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqEpgmTransportConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqInprocTransportConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqIpcTransportConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqPgmTransportConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Arrays;

public class ZeroMqConfigurationUtils extends CommonConfigurationUtils {

   static final String ZERO_MQ_TCP_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.zeromq.ZeroMqTcpTransportConfiguration";
   static final String ZERO_MQ_IPC_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.zeromq.ZeroMqIcpTransportConfiguration";
   static final String ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.zeromq.ZeroMqPgmTransportConfiguration";
   static final String ZERO_MQ_EPGM_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.zeromq.ZeroMqEpgmTransportConfiguration";
   static final String ZERO_MQ_INPROC_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.zeromq.ZeroMqInprocTransportConfiguration";

   static final String CONNECTION_TYPE_FIELD_NAME = "connectionType";
   static final String BIND_CONFIGURATION_FIELD_NAME = "bindConfiguration";
   static final String BIND_CONFIGURATION_INTERFACE_FIELD_NAME = "interface";
   static final String CONNECT_CONFIGURATION_FIELD_NAME = "connectConfiguration";
   static final String CONNECT_CONFIGURATION_REMOTE_ADDRESS_FIELD_NAME = "remoteAddress";
   static final String PORT_FIELD_NAME = "port";
   static final String PATH_FIELD_NAME = "path";
   static final String ADDRESS_NAME_FIELD_NAME = "addressName";
   static final String GROUP_ADDRESS_FIELD_NAME = "groupAddress";
   static final String SOURCE_INTERFACE_FIELD_NAME = "sourceInterface";
   static final String TARGET_INTERFACE_FIELD_NAME = "targetInterface";
   
   static boolean isZeroMqConfiguration(IData type) {
      return Arrays.asList(
         ZERO_MQ_TCP_CONFIGURATION_QUALIFIED_NAME,
         ZERO_MQ_IPC_CONFIGURATION_QUALIFIED_NAME,
         ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME,
         ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME,
         ZERO_MQ_EPGM_CONFIGURATION_QUALIFIED_NAME,
         ZERO_MQ_INPROC_CONFIGURATION_QUALIFIED_NAME).contains(type.getFullyQualifiedName());
   }

   public static ZeroMqConfiguration getZeroMqTcpConfiguration(IPropertyDataValue value) {
      ZeroMqTcpTransportConfiguration configuration = new ZeroMqTcpTransportConfiguration();
      setConnectionType(configuration, value);
      IPropertyDataValue bind = value.getData(getField(value, BIND_CONFIGURATION_FIELD_NAME));
      IPropertyDataValue bindInterface = bind.getData(getField(bind, BIND_CONFIGURATION_INTERFACE_FIELD_NAME));
      IPropertyDataValue connect = value.getData(getField(value, CONNECT_CONFIGURATION_FIELD_NAME));
      IPropertyDataValue connectAddress = connect.getData(getField(connect, CONNECT_CONFIGURATION_REMOTE_ADDRESS_FIELD_NAME));
      BigInteger port = value.getPrimitive(getField(value, PORT_FIELD_NAME)).getInteger();
      configuration.setBindConfiguration(getNetworkInterface(bindInterface));
      configuration.setConnectConfiguration(getNetworkAddress(connectAddress));
      configuration.setPort(port.intValueExact());
      return configuration;
   }

   public static ZeroMqConfiguration getZeroMqIpcConfiguration(IPropertyDataValue value) {
      ZeroMqIpcTransportConfiguration configuration = new ZeroMqIpcTransportConfiguration();
      setConnectionType(configuration, value);
      String path = value.getPrimitive(getField(value, PATH_FIELD_NAME)).getString();
      configuration.setPath(path);
      return configuration;
   }

   public static ZeroMqConfiguration getZeroMqInprocConfiguration(IPropertyDataValue value) {
      ZeroMqInprocTransportConfiguration configuration = new ZeroMqInprocTransportConfiguration();
      setConnectionType(configuration, value);
      String addressName = value.getPrimitive(getField(value, ADDRESS_NAME_FIELD_NAME)).getString();
      configuration.setAddressName(addressName);
      return configuration;
   }

   public static ZeroMqConfiguration getZeroMqPgmConfiguration(IPropertyDataValue value) {
      ZeroMqPgmTransportConfiguration configuration = new ZeroMqPgmTransportConfiguration();
      setConnectionType(configuration, value);
      String groupAddress = value.getPrimitive(getField(value, GROUP_ADDRESS_FIELD_NAME)).getString();
      BigInteger port = value.getPrimitive(getField(value, PORT_FIELD_NAME)).getInteger();
      IPropertyDataValue source = value.getData(getField(value, SOURCE_INTERFACE_FIELD_NAME));
      IPropertyDataValue target = value.getData(getField(value, TARGET_INTERFACE_FIELD_NAME));
      configuration.setGroupAddress(groupAddress);
      configuration.setPort(port.intValueExact());
      configuration.setSourceInterface(getNetworkInterface(source));
      configuration.setTargetInterface(getNetworkInterface(target));
      return configuration;
   }

   public static ZeroMqConfiguration getZeroMqEpgmConfiguration(IPropertyDataValue value) {
      ZeroMqEpgmTransportConfiguration configuration = new ZeroMqEpgmTransportConfiguration();
      setConnectionType(configuration, value);
      String groupAddress = value.getPrimitive(getField(value, GROUP_ADDRESS_FIELD_NAME)).getString();
      BigInteger port = value.getPrimitive(getField(value, PORT_FIELD_NAME)).getInteger();
      IPropertyDataValue source = value.getData(getField(value, SOURCE_INTERFACE_FIELD_NAME));
      IPropertyDataValue target = value.getData(getField(value, TARGET_INTERFACE_FIELD_NAME));
      configuration.setGroupAddress(groupAddress);
      configuration.setPort(port.intValueExact());
      configuration.setSourceInterface(getNetworkInterface(source));
      configuration.setTargetInterface(getNetworkInterface(target));
      return configuration;
   }

   private static void setConnectionType(ZeroMqConfiguration configuration, IPropertyDataValue value) {
      String connectionType = value.getEnumeration(getField(value, CONNECTION_TYPE_FIELD_NAME)).getValue();
      switch (connectionType) {
      case "SOURCE_BINDS_TARGET_CONNECTS":
         configuration.setConnectionType(ConnectionType.SOURCE_BINDS_TARGET_CONNECTS);
         break;
      case "SOURCE_CONNECTS_TARGET_BINDS":
         configuration.setConnectionType(ConnectionType.SOURCE_CONNECTS_TARGET_BINDS);
         break;
      default:
         throw new IllegalArgumentException("Unknown connection type: " + connectionType);
      }

   }

}
