package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;

/**
 * Defines the configuration for a link that should use ZeroMQ's EPGM transport mechanism. This is a reliable protocol
 * which uses UDP.
 */
public class ZeroMqEpgmTransportConfiguration extends ZeroMqConfiguration {

   private String groupAddress;
   private int port;
   private NetworkInterface sourceInterface;
   private NetworkInterface targetInterface;

   /**
    * An IPv4 or IPv6 formatted multicast group address that should be used to implement the link. This should not be a DNS address or hostname.
    */
   public String getGroupAddress() {
      return groupAddress;
   }

   public ZeroMqEpgmTransportConfiguration setGroupAddress(String groupAddress) {
      this.groupAddress = groupAddress;
      return this;
   }

   /**
    * A port number.
    */
   public int getPort() {
      return port;
   }

   public ZeroMqEpgmTransportConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

   /**
    * The network interface of the source of the link to use for the connection.
    */
   public NetworkInterface getSourceInterface() {
      return sourceInterface;
   }

   public ZeroMqEpgmTransportConfiguration setSourceInterface(NetworkInterface sourceInterface) {
      this.sourceInterface = sourceInterface;
      return this;
   }

   /**
    * The network interface of the target of the link to use for the connection.
    */
   public NetworkInterface getTargetInterface() {
      return targetInterface;
   }

   public ZeroMqEpgmTransportConfiguration setTargetInterface(NetworkInterface targetInterface) {
      this.targetInterface = targetInterface;
      return this;
   }
}
