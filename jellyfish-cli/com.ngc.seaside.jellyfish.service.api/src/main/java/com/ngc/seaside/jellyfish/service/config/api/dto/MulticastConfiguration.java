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
package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#MULTICAST}
 * transport configuration.
 */
public class MulticastConfiguration {

   private String groupAddress;
   private NetworkInterface targetInterface;
   private NetworkInterface sourceInterface;
   private int port;

   public String getGroupAddress() {
      return groupAddress;
   }

   public MulticastConfiguration setGroupAddress(String groupAddress) {
      this.groupAddress = groupAddress;
      return this;
   }

   public NetworkInterface getTargetInterface() {
      return targetInterface;
   }

   public MulticastConfiguration setTargetInterface(NetworkInterface targetInterface) {
      this.targetInterface = targetInterface;
      return this;
   }

   public NetworkInterface getSourceInterface() {
      return sourceInterface;
   }

   public MulticastConfiguration setSourceInterface(NetworkInterface sourceInterface) {
      this.sourceInterface = sourceInterface;
      return this;
   }

   public int getPort() {
      return port;
   }

   public MulticastConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof MulticastConfiguration)) {
         return false;
      }
      MulticastConfiguration that = (MulticastConfiguration) o;
      return Objects.equals(this.groupAddress, that.groupAddress)
            && Objects.equals(this.targetInterface, that.targetInterface)
            && Objects.equals(this.sourceInterface, that.sourceInterface)
            && this.port == that.port;
   }

   @Override
   public int hashCode() {
      return Objects.hash(groupAddress,
                          targetInterface, sourceInterface, port);
   }

   @Override
   public String toString() {
      return "MulticastConfiguration[groupAddress=" + groupAddress
            + ", port=" + port
            + ", tragetInterface=" + targetInterface.toString()
            + ", sourceInterface=" + sourceInterface.toString() + "]";
   }
}
