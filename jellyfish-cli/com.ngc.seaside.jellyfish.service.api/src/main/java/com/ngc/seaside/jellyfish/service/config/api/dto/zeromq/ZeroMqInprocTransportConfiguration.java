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
      return "ZeroMqInprocTransportConfiguration[connectionType=" + this.getConnectionType() +
         ",groupAddress=" + this.getAddressName() +
         "]";
   }
}
