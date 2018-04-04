package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;

/**
 * Defines the configuration for a link that should use ZeroMQ's TCP transport mechanism. This is the most common
 * ZeroMQ transport mechanism.
 */
public class ZeroMqTcpTransportConfiguration extends ZeroMqConfiguration {

   private NetworkInterface bindConfiguration;
   private NetworkAddress connectConfiguration;
   private int port;

   /**
    * Defines the configuration for the component that acts as the server for the link. This may be the target or the
    * source depending on the connection type of the link.
    */
   public NetworkInterface getBindConfiguration() {
      return bindConfiguration;
   }

   public ZeroMqTcpTransportConfiguration setBindConfiguration(NetworkInterface bindConfiguration) {
      this.bindConfiguration = bindConfiguration;
      return this;
   }

   /**
    * Defines the configuration for the component that acts as the client for the link. This may be the target or the
    * source depending on the connection type of the link.
    */
   public NetworkAddress getConnectConfiguration() {
      return connectConfiguration;
   }

   public ZeroMqTcpTransportConfiguration setConnectConfiguration(NetworkAddress connectConfiguration) {
      this.connectConfiguration = connectConfiguration;
      return this;
   }

   /**
    * The port number.
    */
   public int getPort() {
      return port;
   }

   public ZeroMqTcpTransportConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

}
