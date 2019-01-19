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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils;

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

   public static final String
         ZERO_MQ_TCP_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.zeromq.ZeroMqTcpTransportConfiguration";
   public static final String
         ZERO_MQ_IPC_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.zeromq.ZeroMqIcpTransportConfiguration";
   public static final String
         ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.zeromq.ZeroMqPgmTransportConfiguration";
   public static final String
         ZERO_MQ_EPGM_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.zeromq.ZeroMqEpgmTransportConfiguration";
   public static final String
         ZERO_MQ_INPROC_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.zeromq.ZeroMqInprocTransportConfiguration";

   public static final String CONNECTION_TYPE_FIELD_NAME = "connectionType";
   public static final String BIND_CONFIGURATION_FIELD_NAME = "bindConfiguration";
   public static final String BIND_CONFIGURATION_INTERFACE_FIELD_NAME = "interface";
   public static final String CONNECT_CONFIGURATION_FIELD_NAME = "connectConfiguration";
   public static final String CONNECT_CONFIGURATION_REMOTE_ADDRESS_FIELD_NAME = "remoteAddress";
   public static final String PORT_FIELD_NAME = "port";
   public static final String PATH_FIELD_NAME = "path";
   public static final String ADDRESS_NAME_FIELD_NAME = "addressName";
   public static final String GROUP_ADDRESS_FIELD_NAME = "groupAddress";
   public static final String SOURCE_INTERFACE_FIELD_NAME = "sourceInterface";
   public static final String TARGET_INTERFACE_FIELD_NAME = "targetInterface";

   /**
    * Returns true if the given data is a type of zero mq configuration.
    * 
    * @param type data
    * @return true if the given data is a type of zero mq configuration
    */
   public static boolean isZeroMqConfiguration(IData type) {
      return Arrays.asList(
            ZERO_MQ_TCP_CONFIGURATION_QUALIFIED_NAME,
            ZERO_MQ_IPC_CONFIGURATION_QUALIFIED_NAME,
            ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME,
            ZERO_MQ_PGM_CONFIGURATION_QUALIFIED_NAME,
            ZERO_MQ_EPGM_CONFIGURATION_QUALIFIED_NAME,
            ZERO_MQ_INPROC_CONFIGURATION_QUALIFIED_NAME).contains(type.getFullyQualifiedName());
   }

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
   public static ZeroMqConfiguration getZeroMqTcpConfiguration(IPropertyDataValue value) {
      ZeroMqTcpTransportConfiguration configuration = new ZeroMqTcpTransportConfiguration();
      setConnectionType(configuration, value);
      IPropertyDataValue bind = value.getData(getField(value, BIND_CONFIGURATION_FIELD_NAME));
      IPropertyDataValue bindInterface = bind.getData(getField(bind, BIND_CONFIGURATION_INTERFACE_FIELD_NAME));
      IPropertyDataValue connect = value.getData(getField(value, CONNECT_CONFIGURATION_FIELD_NAME));
      IPropertyDataValue
            connectAddress =
            connect.getData(getField(connect, CONNECT_CONFIGURATION_REMOTE_ADDRESS_FIELD_NAME));
      BigInteger port = value.getPrimitive(getField(value, PORT_FIELD_NAME)).getInteger();
      configuration.setBindConfiguration(getNetworkInterface(bindInterface));
      configuration.setConnectConfiguration(getNetworkAddress(connectAddress));
      configuration.setPort(port.intValueExact());
      return configuration;
   }

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
   public static ZeroMqConfiguration getZeroMqIpcConfiguration(IPropertyDataValue value) {
      ZeroMqIpcTransportConfiguration configuration = new ZeroMqIpcTransportConfiguration();
      setConnectionType(configuration, value);
      String path = value.getPrimitive(getField(value, PATH_FIELD_NAME)).getString();
      configuration.setPath(path);
      return configuration;
   }

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
   public static ZeroMqConfiguration getZeroMqInprocConfiguration(IPropertyDataValue value) {
      ZeroMqInprocTransportConfiguration configuration = new ZeroMqInprocTransportConfiguration();
      setConnectionType(configuration, value);
      String addressName = value.getPrimitive(getField(value, ADDRESS_NAME_FIELD_NAME)).getString();
      configuration.setAddressName(addressName);
      return configuration;
   }

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
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

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
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
