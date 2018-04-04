package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

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

}
