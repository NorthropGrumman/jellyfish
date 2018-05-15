package com.ngc.seaside.jellyfish.service.config.api;

/**
 * Defines the different transport types the default {@code ITransportConfigurationService} supports.
 */
public enum TransportConfigurationType {

   /**
    * UDP multicast.
    */
   MULTICAST,

   /**
    * HTTP REST.
    */
   REST,
   
   /**
    * Zero MQ.
    */
   ZERO_MQ,

   /**
    * Telemetry.
    */
   TELEMETRY
}
