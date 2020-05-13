/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
