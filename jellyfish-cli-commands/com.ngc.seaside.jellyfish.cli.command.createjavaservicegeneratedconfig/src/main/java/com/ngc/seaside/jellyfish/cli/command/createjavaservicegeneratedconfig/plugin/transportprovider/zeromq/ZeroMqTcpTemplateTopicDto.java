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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq.ZeroMqTcpConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;

import java.util.Set;

public class ZeroMqTcpTemplateTopicDto {

   enum Socket {
      PUBLISH("com.ngc.seaside.service.transport.impl.topic.zeromq.socket.PUB"),
      SUBSCRIBE("com.ngc.seaside.service.transport.impl.topic.zeromq.socket.SUB"),
      REQUEST("com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REQ"),
      RESPOND("com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REP");

      private String type;

      Socket(String type) {
         this.type = type;
      }

      public String getType() {
         return type;
      }

      public boolean isSend() {
         return this == PUBLISH || this == RESPOND;
      }
   }

   private ZeroMqTcpTransportConfiguration configuration;
   private String topicVariableName;
   private Socket socket;
   private Set<TransportTopicDto> topics;

   /**
    * Constructor.
    * 
    * @param transportTopicConfigurationDto transport topic configuration dto
    * @param socket type of socket
    */
   public ZeroMqTcpTemplateTopicDto(ITransportTopicConfigurationDto<
            ZeroMqTcpConfigurationDto> transportTopicConfigurationDto, Socket socket) {
      this.topicVariableName = transportTopicConfigurationDto.getTopicVariableName();
      this.configuration = transportTopicConfigurationDto.getValue().getConfiguration();
      this.socket = socket;
      this.topics = transportTopicConfigurationDto.getTransportTopics();
   }

   public String getVariableName() {
      return topicVariableName;
   }

   public Set<TransportTopicDto> getTransportTopics() {
      return topics;
   }

   public String getSocketType() {
      return socket.getType();
   }

   public String getConnectionType() {
      return shouldBind() ? "BIND" : "CONNECT";
   }

   public String getNetworkInterface() {
      return shouldBind() ? configuration.getBindConfiguration().getName()
               : configuration.getConnectConfiguration().getAddress();
   }

   public int getPort() {
      return configuration.getPort();
   }

   /**
    * Returns whether or not the topic should bind or connect.
    * @return whether or not the topic should bind or connect
    */
   public boolean shouldBind() {
      switch (configuration.getConnectionType()) {
         case SOURCE_BINDS_TARGET_CONNECTS:
            return isSend();
         case SOURCE_CONNECTS_TARGET_BINDS:
            return !isSend();
         default:
            throw new AssertionError();
      }
   }

   public boolean isSend() {
      return socket.isSend();
   }

}
