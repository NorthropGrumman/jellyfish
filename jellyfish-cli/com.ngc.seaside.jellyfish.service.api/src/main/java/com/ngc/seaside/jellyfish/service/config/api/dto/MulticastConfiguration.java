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
