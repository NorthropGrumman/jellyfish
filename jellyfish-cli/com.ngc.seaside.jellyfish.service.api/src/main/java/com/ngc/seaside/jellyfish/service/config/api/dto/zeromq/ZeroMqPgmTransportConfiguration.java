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
package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.google.common.base.Objects;

import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;

/**
 * Defines the configuration for a link that should use ZeroMQ's PGM transport mechanism. This is a reliable protocol
 * which uses UDP. In most cases, EPGM should be preferred.
 */
public class ZeroMqPgmTransportConfiguration extends ZeroMqConfiguration {

   private String groupAddress;
   private int port;
   private NetworkInterface sourceInterface;
   private NetworkInterface targetInterface;

   /**
    * An IPv4 or IPv6 formatted multicast group address that should be used to implement the link.
    * This should not be a DNS address or hostname.
    */
   public String getGroupAddress() {
      return groupAddress;
   }

   public ZeroMqPgmTransportConfiguration setGroupAddress(String groupAddress) {
      this.groupAddress = groupAddress;
      return this;
   }

   /**
    * A port number.
    */
   public int getPort() {
      return port;
   }

   public ZeroMqPgmTransportConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

   /**
    * The network interface of the source of the link to use for the connection.
    */
   public NetworkInterface getSourceInterface() {
      return sourceInterface;
   }

   public ZeroMqPgmTransportConfiguration setSourceInterface(NetworkInterface sourceInterface) {
      this.sourceInterface = sourceInterface;
      return this;
   }

   /**
    * The network interface of the target of the link to use for the connection.
    */
   public NetworkInterface getTargetInterface() {
      return targetInterface;
   }

   public ZeroMqPgmTransportConfiguration setTargetInterface(NetworkInterface targetInterface) {
      this.targetInterface = targetInterface;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ZeroMqPgmTransportConfiguration)) {
         return false;
      }
      ZeroMqPgmTransportConfiguration that = (ZeroMqPgmTransportConfiguration) o;
      return Objects.equal(this.getConnectionType(), that.getConnectionType())
            && Objects.equal(this.getGroupAddress(), that.getGroupAddress())
            && Objects.equal(this.getPort(), that.getPort())
            && Objects.equal(this.getSourceInterface(), that.getSourceInterface())
            && Objects.equal(this.getTargetInterface(), that.getTargetInterface());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getConnectionType(),
                              this.getGroupAddress(),
                              this.getPort(),
                              this.getSourceInterface(),
                              this.getTargetInterface());
   }

   @Override
   public String toString() {
      return "ZeroMqPgmTransportConfiguration[connectionType=" + this.getConnectionType()
            + ",groupAddress=" + this.getGroupAddress()
            + ",port=" + this.getPort()
            + ",sourceInterface=" + this.getSourceInterface().getName()
            + ",targetInterface=" + this.getTargetInterface().getName()
            + "]";
   }
}
