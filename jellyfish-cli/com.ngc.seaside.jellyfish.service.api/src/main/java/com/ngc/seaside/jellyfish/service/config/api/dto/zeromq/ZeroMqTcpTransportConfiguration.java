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

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ZeroMqTcpTransportConfiguration)) {
         return false;
      }
      ZeroMqTcpTransportConfiguration that = (ZeroMqTcpTransportConfiguration) o;
      return Objects.equal(this.getConnectionType(), that.getConnectionType())
            && Objects.equal(this.getBindConfiguration(), that.getBindConfiguration())
            && Objects.equal(this.getConnectConfiguration(), that.getConnectConfiguration())
            && Objects.equal(this.getPort(), that.getPort());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getConnectionType(),
                              this.getBindConfiguration(),
                              this.getConnectConfiguration(),
                              this.getPort());
   }

   @Override
   public String toString() {
      return "ZeroMqTcpTransportConfiguration[connectionType=" + this.getConnectionType()
            + ",bindInterface=" + this.getBindConfiguration().getName()
            + ",connectionAddress=" + this.getConnectConfiguration().getAddress()
            + ",port=" + this.getPort()
            + "]";
   }
}
