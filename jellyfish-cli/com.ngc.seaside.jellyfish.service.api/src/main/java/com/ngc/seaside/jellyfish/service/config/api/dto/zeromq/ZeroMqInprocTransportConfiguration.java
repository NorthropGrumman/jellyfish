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
package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.google.common.base.Objects;

/**
 * Defines the configuration for a link that should use ZeroMQ's in memory transport mechanism.
 */
public class ZeroMqInprocTransportConfiguration extends ZeroMqConfiguration {

   private String addressName;

   /**
    * The unique name of the memory buffer to use to exchange messages.
    */
   public String getAddressName() {
      return addressName;
   }

   public ZeroMqInprocTransportConfiguration setAddressName(String addressName) {
      this.addressName = addressName;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ZeroMqInprocTransportConfiguration)) {
         return false;
      }
      ZeroMqInprocTransportConfiguration that = (ZeroMqInprocTransportConfiguration) o;
      return Objects.equal(this.getConnectionType(), that.getConnectionType())
            && Objects.equal(this.getAddressName(), that.getAddressName());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getConnectionType(), this.getAddressName());
   }

   @Override
   public String toString() {
      return "ZeroMqInprocTransportConfiguration[connectionType=" + this.getConnectionType()
            + ",groupAddress=" + this.getAddressName()
            + "]";
   }
}
